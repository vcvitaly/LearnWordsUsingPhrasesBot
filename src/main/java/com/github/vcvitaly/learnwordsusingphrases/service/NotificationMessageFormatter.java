package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.dto.MessageFromDetails;

/**
 * NotificationMessageFormatter.
 *
 * @author Vitalii Chura
 */
public interface NotificationMessageFormatter {

    String formatMessage(MessageFromDetails fromDetails, String text);
}
