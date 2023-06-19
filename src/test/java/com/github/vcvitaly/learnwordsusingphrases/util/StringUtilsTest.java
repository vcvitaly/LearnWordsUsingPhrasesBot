package com.github.vcvitaly.learnwordsusingphrases.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * StringUtilsTest.
 *
 * @author Vitalii Chura
 */
class StringUtilsTest {

    @Test
    void classNameIsConvertedToLowerCamel() {
        final var className = "SimpleClass";

        assertThat(StringUtils.classNameToLowerCamelCase(className)).isEqualTo("simpleClass");
    }
}