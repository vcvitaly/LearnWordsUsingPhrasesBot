package com.github.vcvitaly.learnwordsusingphrases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * TestUtil.
 *
 * @author Vitalii Chura
 */
@UtilityClass
public class TestUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public static <T> List<T> convertToListOfObjects(String s, Class<T> clazz) {
        final CollectionType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);

        return OBJECT_MAPPER.readValue(s, javaType);
    }
}
