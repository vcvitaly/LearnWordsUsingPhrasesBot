package com.github.vcvitaly.learnwordsusingphrases.client;

import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.WordDefinitionResponseItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * FreeDictionaryApiClient.
 *
 * @author Vitalii Chura
 */
@FeignClient(name = "free-dictionary-api-client", url = "${route.freedictionaryapi}")
public interface FreeDictionaryApiClient {

    @GetMapping("/api/v2/entries/en/{word}")
    List<WordDefinitionResponseItem> getWordDefinitionResponse(@PathVariable(name = "word") String word);
}
