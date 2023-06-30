package com.github.vcvitaly.learnwordsusingphrases.repository;

import com.github.vcvitaly.learnwordsusingphrases.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository.
 *
 * @author Vitalii Chura
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByChatId(Long chatId);

    Optional<User> findByChatId(Long chatId);

    @Query("SELECT COUNT(u.id) > 0 FROM User u JOIN u.words w WHERE u.chatId = :chatId AND w.word = :word")
    boolean hasWordSaved(@Param("chatId") Long chatId, @Param("word") String word);
}
