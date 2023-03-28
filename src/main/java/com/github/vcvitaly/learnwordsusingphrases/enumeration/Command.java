package com.github.vcvitaly.learnwordsusingphrases.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Command.
 *
 * @author Vitalii Chura
 */
@RequiredArgsConstructor
@Getter
public enum Command {
    START("/start", "command_text/start.txt"),
    HELP("/help", "command_text/help.txt"),
    SUBSCRIBED_WORDS("/subscribed_words", null);

    private final String command;
    private final String descriptionFilePath;
}
