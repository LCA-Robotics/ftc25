package org.lexingtonchristian.ftc.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.lexingtonchristian.ftc.action.Action;

import java.io.IOException;
import java.util.List;

public class ActionDeserializer extends StdDeserializer<Action> {

    protected ActionDeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public Action deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {

        JsonNode node = parser.getCodec().readTree(parser);

        return null;

    }

}
