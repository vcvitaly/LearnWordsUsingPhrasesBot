package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.entity.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

/**
 * DaoFacadeService.
 *
 * @author Vitalii Chura
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DaoFacadeService {

    private final UserService userService;

    private final WordService wordService;

    public boolean hasWordSaved(Long chatId, String word) {
        return userService.hasWordSaved(chatId, word);
    }

    @Transactional
    public void saveWordForUser(Long chatId, String word) {
        if (!hasWordSaved(chatId, word)) {
            final var user = userService.getOrElseNew(chatId);
            final var aWord = wordService.getOrElseNew(word);
            user.getWords().add(aWord);
            userService.save(user);
        } else {
            LOG.debug("Received a request to save a word [{}] for chatId [{}], but it has already been saved", word, chatId);
        }
    }

    @Transactional
    public void deleteWordFromUser(Long chatId, String word) {
        if (hasWordSaved(chatId, word)) {
            final var user = userService.get(chatId);
            user.getWords().removeIf(aWord -> aWord.getWord().equals(word));
            if (!user.getWords().isEmpty()) {
                userService.save(user);
            } else {
                userService.delete(user);
            }
        } else {
            LOG.debug("Received a request to delete a word [{}] for chatId [{}], but it wasn't saved", word, chatId);
        }
    }

    @Transactional(readOnly = true)
    public Set<String> getUserWords(Long chatId) {
        if (userService.exists(chatId)) {
            return userService.get(chatId).getWords().stream()
                    .map(Word::getWord)
                    .collect(Collectors.toSet());
        }

        return emptySet();
    }

    @Transactional
    public boolean deleteAllUserWords(Long chatId) {
        if (userService.exists(chatId)) {
            userService.delete(chatId);
            return true;
        }

        return false;
    }
}
