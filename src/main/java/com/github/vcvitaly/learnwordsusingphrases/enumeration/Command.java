package com.github.vcvitaly.learnwordsusingphrases.enumeration;

import com.github.vcvitaly.learnwordsusingphrases.util.Constants;
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
    START("start", "command_text/start.txt", "start command"),
    HELP("help", "command_text/help.txt", "get help"),
    MY_WORDS("my_words", null, "list my words"),
    SAVE_WORD("save_word", null, Constants.SAVE_WORD_TEXT),
    DELETE_WORD("delete_word", null, Constants.DELETE_WORD_TEXT),
    DELETE_ALL("delete_all", null, "Delete all words from my list");


    private final String data;
    private final String descriptionMessageFilePath;
    private final String description;

    public String getCommand() {
        return "/" + data;
    }
}
