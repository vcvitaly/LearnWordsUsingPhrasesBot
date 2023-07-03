package com.github.vcvitaly.learnwordsusingphrases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

import static com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil.readResourceAsString;

/**
 * DefinitionPreparationHelper.
 *
 * @author Vitalii Chura
 */
@UtilityClass
public class DefinitionPreparationHelper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> List<T> prepareExpectedDefinitions(String resource, Class<T[]> clazz)
            throws JsonProcessingException {
        var jsonStr = readResourceAsString(resource);
        return Arrays.asList(OBJECT_MAPPER.readValue(jsonStr, clazz));
    }
}
