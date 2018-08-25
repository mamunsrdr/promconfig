package com.finreach.promconfig.rest;

import com.finreach.promconfig.app.dto.Response;
import com.finreach.promconfig.app.dto.PromTarget;
import com.finreach.promconfig.service.PromConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/config")
public class PromConfigController {

    private final PromConfigService promConfigService;

    @Autowired
    public PromConfigController(PromConfigService promConfigService) {
        this.promConfigService = promConfigService;
    }

    @PostMapping("/add")
    public Response addConfig(@RequestBody PromTarget promTarget) {
        return this.promConfigService.addConfig(promTarget);
    }

    @DeleteMapping("/delete")
    public Response deleteConfig(@RequestBody PromTarget promTarget) {
        return this.promConfigService.deleteConfig(promTarget);
    }

    @PostMapping("/modify")
    public Response modifyConfig(@RequestBody PromTarget promTarget) {
        return this.promConfigService.modifyConfig(promTarget);
    }
}
