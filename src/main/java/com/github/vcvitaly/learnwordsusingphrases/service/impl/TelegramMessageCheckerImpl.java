package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.service.TelegramMessageChecker;
import org.springframework.stereotype.Service;

/**
 * TelegramMessageCheckerImpl.
 *
 * @author Vitalii Chura
 */
@Service
public class TelegramMessageCheckerImpl implements TelegramMessageChecker {

    @Override
    public String replaceIllegalChars(String text) {
        return text
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)")
                .replaceAll("\\.", "\\\\.")
                .replaceAll(",", "\\\\,")
                .replaceAll("\\[", "\\\\[")
                .replaceAll("]", "\\\\]")
                .replaceAll("\\{", "\\\\{")
                .replaceAll("}", "\\\\}")
                .replaceAll("\\?", "\\\\?")
                .replaceAll("!", "\\\\!")
                .replaceAll("\\+", "\\\\+")
                .replaceAll("-", "\\\\-")
                .replaceAll("_", "\\\\_")
                .replaceAll("\\*", "\\\\_");
    }
}
