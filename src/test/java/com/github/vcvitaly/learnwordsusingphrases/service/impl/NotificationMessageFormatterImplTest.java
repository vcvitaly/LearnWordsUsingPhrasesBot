package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * NotificationMessageFormatterImplTest.
 *
 * @author Vitalii Chura
 */
class NotificationMessageFormatterImplTest {

    private final NotificationMessageFormatterImpl notificationMessageFormatter = new NotificationMessageFormatterImpl();

    @Test
    void textIsFormatted() {
        final var formattedMessage = notificationMessageFormatter.formatMessage("who_am_i", "text");

        Assertions.assertThat(formattedMessage).isEqualTo("*who\\_am\\_i* sent *text*");
    }
}