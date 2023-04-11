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
    START("start", "command_text/start.txt"),
    HELP("help", "command_text/help.txt"),
    SUBSCRIBED_WORDS("subscribed_words", null),
    SUBSCRIBE("subscribe", null),
    UNSUBSCRIBE("unsubscribe", null),
    UNSUBSCRIBE_ALL("unsubscribe_all", null);


    private final String data;
    private final String descriptionFilePath;

    public String getCommand() {
        return "/" + data;
    }
}
