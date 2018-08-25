package com.finreach.promconfig.app.dto;

import java.io.Serializable;
import java.util.List;

public class PromStaticConfig implements Serializable {

    private static final long serialVersionUID = 53581855517148492L;

    List<PromTarget> static_configs;

    public List<PromTarget> getStatic_configs() {
        return static_configs;
    }

    public void setStatic_configs(List<PromTarget> static_configs) {
        this.static_configs = static_configs;
    }
}
