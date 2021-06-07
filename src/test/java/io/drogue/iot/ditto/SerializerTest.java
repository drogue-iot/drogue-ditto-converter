package io.drogue.iot.ditto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventBuilder;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SerializerTest {

    @Test
    void testFunction() throws JsonProcessingException {

        DrogueDevice device = new DrogueDevice();
        device.setApp("app");
        device.setDevice("device");
        device.setProperty("temp", "23.0");

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(DrogueDevice.class, new DittoThingSerializer());
        mapper.registerModule(module);
        String dittoJson = mapper.writeValueAsString(device);

        CloudEvent<String> event = CloudEventBuilder.create()
                .build(dittoJson);

        Assertions.assertEquals(
            "{\"path\":\"/features\",\"topic\":\"app/device/things/twin/commands/modify\",\"value\":{\"temp\":{\"properties\":{\"value\":23.0}}}}"
            , event.data()
        );

    }

}
