package common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class DynamoObjectMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static <T> T mapDynamoItem(Map<String, Object> item, Class<T> dataObject) {
        return MAPPER.convertValue(item, dataObject);
    }
}
