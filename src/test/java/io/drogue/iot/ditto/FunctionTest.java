package io.drogue.iot.ditto;

import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventBuilder;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

@QuarkusTest
public class FunctionTest {

    String topic = "app/device/things/twin/commands/modify";

    @Test
    void testFunction() throws Exception {
        DrogueDevice device = new DrogueDevice();
        device.setProperty("temp", "23.0");
        Map<String, String> extensions = new HashMap<String, String>();
        extensions.put("application", "app");
        extensions.put("device", "device");

        CloudEvent<DrogueDevice> event = CloudEventBuilder
            .create()
            .specVersion("1.0")
            .dataSchema("ditto:simple-thing")
            .extensions(extensions)
            .build(device);

        DittoCommand command = (new DittoConverterFunction().convert(event)).data();
        Assertions.assertEquals(topic, command.getTopic());
    }

    @Test
    public void testFunctionIntegration() {
        RestAssured.given().contentType("application/json")
                .body("{\"temp\":23.0}")
                .header("ce-id", "42")
                .header("ce-specversion", "1.0")
                .header("ce-dataschema", "ditto:simple-thing")
                .header("ce-application", "app")
                .header("ce-device", "device")
                .post("/")
                .then().statusCode(200)
                .header("ce-id", notNullValue())
                .header("ce-specversion", equalTo("1.0"))
                .body("topic", equalTo(topic));
    }

}
