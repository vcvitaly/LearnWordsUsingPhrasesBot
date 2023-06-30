package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.entity.User;

/**
 * UserService.
 *
 * @author Vitalii Chura
 */
public interface UserService {

    User get(Long chatId);

    User getOrElseNew(Long chatId);

    boolean hasWordSaved(Long chatId, String word);

    void save(User user);

    boolean exists(Long chatId);

    void delete(Long chatId);

    void delete(User user);
}
