package io.drogue.iot.ditto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class DrogueDevice {
    Map<String, Object> properties = new HashMap<>();

    String app;
    String device;

    @JsonAnySetter
    void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Map<String,Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String,Object> properties) {
        this.properties = properties;
    }

    public String getApp() {
        return this.app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

}
