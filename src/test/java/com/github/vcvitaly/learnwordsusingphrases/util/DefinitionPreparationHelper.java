package com.github.vcvitaly.learnwordsusingphrases.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil.readResourceAsString;

/**
 * DefinitionPreparationHelper.
 *
 * @author Vitalii Chura
 */
public final class DefinitionPreparationHelper {

    private DefinitionPreparationHelper() {
    }

    public static <T> List<T> prepareExpectedDefinitions(String resource, Class<T[]> clazz)
            throws JsonProcessingException {
        var jsonStr = readResourceAsString(resource);
        return Arrays.asList(new ObjectMapper().readValue(jsonStr, clazz));
    }
}
