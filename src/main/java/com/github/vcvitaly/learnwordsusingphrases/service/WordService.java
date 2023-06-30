package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.entity.Word;

/**
 * WordService.
 *
 * @author Vitalii Chura
 */
public interface WordService {

    Word getOrElseNew(String word);

    void save(Word word);
}
