package com.github.vcvitaly.learnwordsusingphrases.util;

import org.apache.commons.lang3.StringUtils;

/**
 * MarkdownUtil.
 *
 * @author Vitalii Chura
 */
public final class MarkdownUtil {

    private static final int WORD_NOT_FOUND = -1;

    private MarkdownUtil() {
    }

    public static String getBold(String s) {
        return "*" + s + "*";
    }

    public static String getHeading3(String s) {
        return "### " + s;
    }

    public static String getHeading5(String s) {
        return "##### " + s;
    }

    public static String markWordWithBold(String s, String word) {
        var indexOfIgnoreCase = StringUtils.indexOfIgnoreCase(s, word);
        if (indexOfIgnoreCase == WORD_NOT_FOUND) {
            return s;
        }

        return s.substring(0, indexOfIgnoreCase) +
                getBold(s.substring(indexOfIgnoreCase, indexOfIgnoreCase + word.length())) +
                s.substring(indexOfIgnoreCase + word.length());
    }
}
