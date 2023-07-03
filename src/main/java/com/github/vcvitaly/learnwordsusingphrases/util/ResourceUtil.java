package com.github.vcvitaly.learnwordsusingphrases.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

/**
 * ResourceUtil.
 *
 * @author Vitalii Chura
 */
@UtilityClass
public class ResourceUtil {

    public static String readResourceAsString(String fileName) {
        try {
            var file = getFileFromResource(fileName);
            return Files.readString(file.toPath());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = ResourceUtil.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}
