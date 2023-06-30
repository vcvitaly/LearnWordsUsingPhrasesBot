package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.entity.Word;
import com.github.vcvitaly.learnwordsusingphrases.repository.WordRepository;
import com.github.vcvitaly.learnwordsusingphrases.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * WordServiceImpl.
 *
 * @author Vitalii Chura
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    @Override
    public Word getOrElseNew(String word) {
        return wordRepository.findByWord(word)
                .orElseGet(() -> {
                    LOG.info("Created a new word [{}]", word);
                    return Word.builder().word(word).build();
                });
    }

    @Override
    public void save(Word word) {
        wordRepository.save(word);
        LOG.info("Saved word {}", word);
    }
}
