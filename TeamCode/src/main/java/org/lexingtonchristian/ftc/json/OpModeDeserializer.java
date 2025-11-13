package org.lexingtonchristian.ftc.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.IOException;
import java.util.List;

public class OpModeDeserializer extends StdDeserializer<OpMode> {

    protected OpModeDeserializer() {
        super(OpMode.class);
    }

    @Override
    public OpMode deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {

        JsonNode node = parser.getCodec().readTree(parser);

        return new LinearOpMode() {
            @Override
            public void runOpMode() throws InterruptedException {

            }
        };

    }

}
