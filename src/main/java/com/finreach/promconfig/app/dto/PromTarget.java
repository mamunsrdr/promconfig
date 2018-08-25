package com.finreach.promconfig.app.dto;

import java.util.List;
import java.util.Map;

public class PromTarget {
    List<String> targets;
    Map<String, String> labels;

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }
}
