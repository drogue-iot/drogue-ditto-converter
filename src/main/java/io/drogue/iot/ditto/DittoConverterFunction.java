package io.drogue.iot.ditto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventBuilder;

public class DittoConverterFunction {

    private static final Logger LOG = LoggerFactory.getLogger(DittoConverterFunction.class);

    @Funq
    public CloudEvent<DittoCommand> convert(CloudEvent<DrogueDevice> input) throws Exception {

        DrogueDevice device = input.data();

        LOG.debug("Device properties: {}", device.getProperties());

        String appId = input.extensions().get("application");
        String deviceId = input.extensions().get("device");
        String dataSchema = input.dataSchema();

        LOG.debug("Converting - dataSchema: {}, appId: {}, deviceId: {}", dataSchema, appId, deviceId);

        if ((dataSchema.isEmpty() || appId.isEmpty() || deviceId.isEmpty())) {
            return null;
        }
        device.setApp(appId);
        device.setDevice(deviceId);

        LOG.debug("Scheme: {}", dataSchema);

        if (!dataSchema.startsWith("ditto:")) {
            // must be prefixed with "ditto:"
            return null;
        }

        DittoCommand command = DittoCommand.from(device);

        CloudEvent<DittoCommand> event = CloudEventBuilder.create()
                .id(input.id())
                .specVersion(input.specVersion())
                .dataSchema(input.dataSchema())
                .source(input.source())
                .subject(input.subject())
                .type(input.type())
                .extensions(input.extensions())
                .build(command);

        return event;
    }

}
