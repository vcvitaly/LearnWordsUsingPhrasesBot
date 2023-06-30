package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.entity.User;
import com.github.vcvitaly.learnwordsusingphrases.repository.UserRepository;
import com.github.vcvitaly.learnwordsusingphrases.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;

/**
 * UserServiceImpl.
 *
 * @author Vitalii Chura
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User get(Long chatId) {
        return userRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with chat id [%d] not found", chatId)
                ));
    }

    @Override
    public User getOrElseNew(Long chatId) {
        return userRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    LOG.info("Created a new user with chatId [{}]", chatId);
                    return User.builder().chatId(chatId).words(new HashSet<>()).build();
                });
    }

    @Override
    public boolean hasWordSaved(Long chatId, String word) {
        return userRepository.hasWordSaved(chatId, word);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
        LOG.info("Saved a user [{}]", user);
    }

    @Override
    public boolean exists(Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    @Override
    public void delete(Long chatId) {
        final var user = get(chatId);
        delete(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
        LOG.info("Deleted the user [{}]", user.getChatId());
    }
}
