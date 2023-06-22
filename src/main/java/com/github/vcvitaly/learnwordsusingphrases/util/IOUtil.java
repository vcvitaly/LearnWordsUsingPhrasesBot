package com.github.vcvitaly.learnwordsusingphrases.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.stream.Collectors;

/**
 * IOUtil.
 *
 * @author Vitalii Chura
 */
@UtilityClass
public class IOUtil {

    public static String fetchReaderData(Reader reader) {
        return new BufferedReader(reader).lines().collect(Collectors.joining());
    }
}
