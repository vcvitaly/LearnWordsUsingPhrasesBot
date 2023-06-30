package com.github.vcvitaly.learnwordsusingphrases.repository;

import com.github.vcvitaly.learnwordsusingphrases.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * WordRepository.
 *
 * @author Vitalii Chura
 */
@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByWord(String word);
}
