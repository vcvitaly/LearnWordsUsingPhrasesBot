package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.dto.MessageFromDetails;
import com.github.vcvitaly.learnwordsusingphrases.service.NotificationMessageFormatter;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * NotificationMessageFormatterImpl.
 *
 * @author Vitalii Chura
 */
@Component
public class NotificationMessageFormatterImpl implements NotificationMessageFormatter {

    @Override
    public String formatMessage(MessageFromDetails fromDetails, String text) {
        final var escapedName = Optional.ofNullable(fromDetails.username())
                .map(this::escapeUnderscores)
                .orElseGet(
                        () -> getFirstAndLastNameOptional(fromDetails.firstName(), fromDetails.lastName())
                                .orElse("Unknown")
                );
        return String.format("*%s* sent *%s*", escapedName, text);
    }

    private String escapeUnderscores(String username) {
        return username.replaceAll("_", "\\\\_");
    }

    private Optional<String> getFirstAndLastNameOptional(String firstName, String lastName) {
        return Optional.ofNullable(firstName)
                .flatMap(fName ->
                        Optional.ofNullable(lastName).map(lName -> fName + lName)
                );
    }
}
