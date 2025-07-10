package ee.aleksale.betgame.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.aleksale.betgame.common.exception.GameSystemException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValuesAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new GameSystemException(e.getMessage());
        }
    }

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new GameSystemException(e.getMessage());
        }
    }
}
