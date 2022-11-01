package com.github.vcvitaly.learnwordsusingphrases.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.stream.Collectors;

/**
 * IOUtil.
 *
 * @author Vitalii Chura
 */
public final class IOUtil {

    private IOUtil() {
    }

    public static String fetchReaderData(Reader reader) {
        return new BufferedReader(reader).lines().collect(Collectors.joining());
    }
}
