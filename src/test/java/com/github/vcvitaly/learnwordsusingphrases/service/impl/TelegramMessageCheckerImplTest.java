package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TelegramMessageCheckerImplTest.
 *
 * @author Vitalii Chura
 */
class TelegramMessageCheckerImplTest {

    private TelegramMessageCheckerImpl checker = new TelegramMessageCheckerImpl();

    @Test
    void charactersAreReplaced() {
        var text = "#*()";

        String replacedText = checker.replaceIllegalChars(text);

        assertThat(replacedText).isEqualTo("#*\\(\\)");
    }
}