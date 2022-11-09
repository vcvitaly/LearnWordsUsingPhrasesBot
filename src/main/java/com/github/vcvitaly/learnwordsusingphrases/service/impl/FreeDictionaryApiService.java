package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.client.FreeDictionaryApiClient;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionItemDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.MeaningsItemDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.WordDefinitionResponseItemDto;
import com.github.vcvitaly.learnwordsusingphrases.exception.DefinitionNotFoundException;
import com.github.vcvitaly.learnwordsusingphrases.service.DefinitionApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

/**
 * FreeDictionaryApiService.
 *
 * @author Vitalii Chura
 */
@Slf4j
@Service
@Order(1)
public class FreeDictionaryApiService implements DefinitionApiService {

    private FreeDictionaryApiClient client;

    public FreeDictionaryApiService(FreeDictionaryApiClient client) {
        this.client = client;
    }

    @Override
    public List<DefinitionDto> getDefinitions(String word) {
        List<WordDefinitionResponseItemDto> wordDefinitionResponses;
        try {
            wordDefinitionResponses = client.getWordDefinitionResponse(word);
        } catch (Exception e) {
            if (e.getCause() instanceof DefinitionNotFoundException) {
                return emptyList();
            }
            throw e;
        }

        log.debug("Received a response for word '{}' from FreeDictionary API: {}", word, wordDefinitionResponses);
        return wordDefinitionResponses.get(0).getMeanings().stream()
                .filter(item -> item.getDefinitions().stream()
                        .anyMatch(definitionItem -> Objects.nonNull(definitionItem.getExample()))
                )
                .map(this::toDefinitionDto)
                .toList();
    }

    private DefinitionDto toDefinitionDto(MeaningsItemDto meaningsItemDto) {
        var definitionDto = new DefinitionDto();
        definitionDto.setPartOfSpeech(meaningsItemDto.getPartOfSpeech());
        var definitionItems = meaningsItemDto.getDefinitions().stream()
                .map(this::toDefinitionItemDto)
                .toList();
        definitionDto.setDefinitionItems(definitionItems);
        return definitionDto;
    }

    private DefinitionItemDto toDefinitionItemDto(
            com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.DefinitionItemDto freeDictionaryDefinitionItemDto) {
        return new DefinitionItemDto(
                freeDictionaryDefinitionItemDto.getDefinition(),
                freeDictionaryDefinitionItemDto.getExample()
        );
    }
}
