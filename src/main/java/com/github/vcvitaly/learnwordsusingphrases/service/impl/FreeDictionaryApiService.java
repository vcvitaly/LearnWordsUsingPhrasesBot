package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.client.FreeDictionaryApiClient;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.DefinitionItem;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.MeaningsItem;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.WordDefinitionResponseItem;
import com.github.vcvitaly.learnwordsusingphrases.exception.DefinitionNotFoundException;
import com.github.vcvitaly.learnwordsusingphrases.service.GoogleDefinitionApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.github.vcvitaly.learnwordsusingphrases.util.Constants.NEW_LINE;

/**
 * FreeDictionaryApiService.
 *
 * @author Vitalii Chura
 */
@Slf4j
@Service
public class FreeDictionaryApiService implements GoogleDefinitionApiService {

    private FreeDictionaryApiClient client;

    public FreeDictionaryApiService(FreeDictionaryApiClient client) {
        this.client = client;
    }

    @Override
    public String getDefinitionsAsString(String word) {
        log.info("Getting a definition for: " + word);
        List<WordDefinitionResponseItem> wordDefinitionResponses;
        try {
            wordDefinitionResponses = client.getWordDefinitionResponse(word);
        } catch (Exception e) {
            if (e.getCause() instanceof DefinitionNotFoundException) {
                return "Definition not found for: " + word;
            }
            throw e;
        }
        var res = new StringBuilder();
        wordDefinitionResponses.get(0).getMeanings().stream()
                .filter(item -> item.getDefinitions().stream()
                        .anyMatch(definitionItem -> Objects.nonNull(definitionItem.getExample()))
                )
                .map(this::getDefinition)
                .forEach(res::append);
        return res.toString();
    }

    private String getDefinition(MeaningsItem item) {
        var definitionSb = new StringBuilder();
        definitionSb.append(item.getPartOfSpeech()).append(NEW_LINE);
        item.getDefinitions().stream()
                .filter(definitionItem -> Objects.nonNull(definitionItem.getExample()))
                .map(this::getDefinition)
                .forEach(definitionSb::append);

        return definitionSb.toString();
    }

    private String getDefinition(DefinitionItem item) {
        return "Definition: " + item.getDefinition() + NEW_LINE +
                "Example: " + item.getExample() + NEW_LINE;
    }
}
