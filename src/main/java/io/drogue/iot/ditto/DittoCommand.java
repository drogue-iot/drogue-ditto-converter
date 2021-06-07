package io.drogue.iot.ditto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DittoCommand {

    protected String app;
    protected String device;

    protected String path;
    protected String topic;

    protected Map<String, Properties> value = new HashMap<String, Properties>();


    protected DittoCommand() {
    }

    public static DittoCommand from(DrogueDevice device) {
        DittoCommand command = new DittoCommand();

        command.app = device.getApp();
        command.device = device.getDevice();
        command.topic = command.app + "/" + command.device + "/things/twin/commands/modify";
        command.path = "/features";

        HashMap<String, Properties> value = new HashMap<String, Properties>();
        for (Entry<String, Object> property : device.properties.entrySet()) {
            Properties properties = new Properties();
            properties.properties.put("value", property.getValue());
            value.put(property.getKey(), properties);
            command.setValue(value);
        }

        return command;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Map<String,Properties> getValue() {
        return this.value;
    }

    public void setValue(Map<String,Properties> value) {
        this.value = value;
    }

}


class Properties {

    Map<String, Object> properties = new HashMap<String, Object>();


    public Map<String,Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String,Object> properties) {
        this.properties = properties;
    }

}