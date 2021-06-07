package io.drogue.iot.ditto;

import java.io.IOException;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DittoThingSerializer  extends StdSerializer<DrogueDevice> {


    public DittoThingSerializer() {
        super(null, false);
    }


    protected DittoThingSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(DrogueDevice device, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("path", "/features");
        gen.writeStringField("topic", device.getApp() + "/" + device.getDevice() + "/things/twin/commands/modify");
        gen.writeFieldName("value");
            gen.writeStartObject();
            for (Entry<String, Object> property  : device.properties.entrySet()) {
                gen.writeFieldName(property.getKey());
                gen.writeStartObject();
                    gen.writeFieldName("properties");
                    gen.writeStartObject();
                        gen.writeFieldName("value");
                        gen.writeRawValue(property.getValue().toString());
                    gen.writeEndObject();
                gen.writeEndObject();
            }
            gen.writeEndObject();
        gen.writeEndObject();
    }

}
