package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.dto.MessageFromDetails;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * NotificationMessageFormatterImplTest.
 *
 * @author Vitalii Chura
 */
class NotificationMessageFormatterImplTest {

    private final NotificationMessageFormatterImpl notificationMessageFormatter = new NotificationMessageFormatterImpl();

    @Test
    void textIsFormatted_WhenUsernameIsPassed() {
        final var formattedMessage = notificationMessageFormatter.formatMessage(
                MessageFromDetails.builder()
                        .username("who_am_i")
                        .build(),
                "text"
        );

        assertThat(formattedMessage).isEqualTo("*who\\_am\\_i* sent *text*");
    }

    @Test
    void textIsFormatted_WhenFirstAndLastNameIsPassed() {
        final var formattedMessage = notificationMessageFormatter.formatMessage(
                MessageFromDetails.builder()
                        .firstName("Jon")
                        .lastName("Doe")
                        .build(),
                "text"
        );

        assertThat(formattedMessage).isEqualTo("*JonDoe* sent *text*");
    }

    @Test
    void textIsFormatted_WhenBothUsernameAndFirstNameAreNull() {
        final var formattedMessage = notificationMessageFormatter.formatMessage(
                MessageFromDetails.builder()
                        .lastName("Doe")
                        .build(),
                "text"
        );

        assertThat(formattedMessage).isEqualTo("*Unknown* sent *text*");
    }

    @Test
    void textIsFormatted_WhenBothUsernameAndLastNameAreNull() {
        final var formattedMessage = notificationMessageFormatter.formatMessage(
                MessageFromDetails.builder()
                        .firstName("Jon")
                        .build(),
                "text"
        );

        assertThat(formattedMessage).isEqualTo("*Unknown* sent *text*");
    }

    @Test
    void textIsFormatted_WhenAllParametersAreNull() {
        final var formattedMessage = notificationMessageFormatter.formatMessage(
                MessageFromDetails.builder().build(),
                "text"
        );

        assertThat(formattedMessage).isEqualTo("*Unknown* sent *text*");
    }
}