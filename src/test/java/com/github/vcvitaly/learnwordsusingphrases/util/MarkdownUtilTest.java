package com.github.vcvitaly.learnwordsusingphrases.util;

import org.junit.jupiter.api.Test;

import static com.github.vcvitaly.learnwordsusingphrases.util.MarkdownUtil.markWordWithBold;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * MarkdownUtilTest.
 *
 * @author Vitalii Chura
 */
class MarkdownUtilTest {


    @Test
    void aWordIsMarkedWithBoldAtTheBeginningOfAPhrase() {
        String phrase = "Hello! How are you?";

        String actual = markWordWithBold(phrase, "hello");

        String expected = "*Hello*! How are you?";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void aWordIsMarkedWithBoldAtInTheMiddleOfAPhrase() {
        String phrase = "Hey, hello, how are you?";

        String actual = markWordWithBold(phrase, "hello");

        String expected = "Hey, *hello*, how are you?";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void aStringIsReturnedEvenIfThereIsNoMatchingWord() {
        String phrase = "How are you?";

        String actual = markWordWithBold(phrase, "hello");

        String expected = "How are you?";
        assertThat(actual).isEqualTo(expected);
    }
}