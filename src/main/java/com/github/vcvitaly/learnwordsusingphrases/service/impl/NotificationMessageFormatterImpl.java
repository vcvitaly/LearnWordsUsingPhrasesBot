package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.service.NotificationMessageFormatter;
import org.springframework.stereotype.Component;

/**
 * NotificationMessageFormatterImpl.
 *
 * @author Vitalii Chura
 */
@Component
public class NotificationMessageFormatterImpl implements NotificationMessageFormatter {

    @Override
    public String formatMessage(String who, String text) {
        final var escapedUsername = who.replaceAll("_", "\\\\_");
        return String.format("*%s* sent *%s*", escapedUsername, text);
    }
}
