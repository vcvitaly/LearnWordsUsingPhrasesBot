package com.github.vcvitaly.learnwordsusingphrases.util;

import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;

/**
 * StringUtils.
 *
 * @author Vitalii Chura
 */
@UtilityClass
public class StringUtils {

    public static String classNameToLowerCamelCase(String className) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className);
    }
}
