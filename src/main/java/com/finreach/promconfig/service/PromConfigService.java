package com.finreach.promconfig.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.finreach.promconfig.app.dto.PromStaticConfig;
import com.finreach.promconfig.app.dto.PromTarget;
import com.finreach.promconfig.app.dto.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.finreach.promconfig.app.dto.Response.getErrorResponse;
import static com.finreach.promconfig.app.dto.Response.getSuccessResponse;

@Service
public class PromConfigService {

    @Value("${app.directory}")
    private String appDir;

    @Value("${app.conf.filename}")
    private String confFileName;

    /**
     * add config - match by target names
     * @param promTarget
     * @return
     */
    public Response addConfig(PromTarget promTarget) {
        try {
            PromStaticConfig staticConfig = this.getExistingConfig();
            List<PromTarget> configs = staticConfig.getStatic_configs();

            if (this.getIndexOf(configs, promTarget) >= 0) {
                return getErrorResponse("Config already exists");
            }
            configs.add(promTarget);
            staticConfig.setStatic_configs(configs);

            this.writePromConfig(staticConfig);
        } catch (Exception ex) {
            //todo: log the error with logger framework
            return getErrorResponse("Failed to add config");
        }
        return getSuccessResponse("Config added successfully");
    }

    public Response deleteConfig(PromTarget promTarget) {
        try {
            PromStaticConfig staticConfig = this.getExistingConfig();
            List<PromTarget> configs = staticConfig.getStatic_configs();

            int index = this.getIndexOf(configs, promTarget);
            if (this.getIndexOf(configs, promTarget) < 0) {
                return getErrorResponse("Config doesn't exist");
            }
            configs.remove(index);
            this.writePromConfig(staticConfig);
        } catch (Exception ex) {
            //todo: log the error with logger framework
            return getErrorResponse("Failed to delete config");
        }
        return getSuccessResponse("Config delete successfully");
    }

    /**
     * modify config - match by target names
     * @param promTarget
     * @return
     */
    public Response modifyConfig(PromTarget promTarget) {
        try {
            PromStaticConfig staticConfig = this.getExistingConfig();
            List<PromTarget> configs = staticConfig.getStatic_configs();

            int index = this.getIndexOf(configs, promTarget);
            if (this.getIndexOf(configs, promTarget) < 0) {
                return getErrorResponse("Config doesn't exist");
            }
            configs.set(index, promTarget);
            this.writePromConfig(staticConfig);
        } catch (Exception ex) {
            //todo: log the error with logger framework
            return getErrorResponse("Failed to modify config");
        }
        return getSuccessResponse("Config modified successfully");
    }


    private int getIndexOf(List<PromTarget> promTargets, PromTarget promTarget) {
        int idx = IntStream.range(0, promTargets.size())
                .filter(i -> promTarget.getTargets().equals(promTargets.get(i).getTargets()))
                .findFirst().orElse(-1);
        return idx;
    }

    /**
     * write config to file
     *
     * @param config PromStaticConfig
     * @throws IOException if file can't open for write
     */
    private void writePromConfig(PromStaticConfig config) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        mapper.writeValue(this.getFile(), config);
    }

    /**
     * read yml config file
     *
     * @return PromStaticConfig
     * @throws IOException if file can't open
     */
    private PromStaticConfig getExistingConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        PromStaticConfig staticConfig = null;
        try {
            staticConfig = mapper.readValue(this.getFile(), PromStaticConfig.class);
        } catch (Exception e) {
            staticConfig = new PromStaticConfig();
            staticConfig.setStatic_configs(new ArrayList<>());
        }
        return staticConfig;
    }

    /**
     * read file from file path
     * create if doesn't exist
     *
     * @return File
     * @throws IOException if file can't open
     */
    private File getFile() throws IOException {
        File dir = new File(appDir);
        if (!dir.exists() || !dir.isDirectory()) dir.mkdir(); //create dir if doesn't exist

        String filePath = appDir + (appDir.endsWith(File.separator) ? "" : File.separator) + confFileName;
        File confFile = new File(filePath);

        //create if does not exists on output dir
        if (!confFile.exists()) confFile.createNewFile();
        return confFile;
    }

}
