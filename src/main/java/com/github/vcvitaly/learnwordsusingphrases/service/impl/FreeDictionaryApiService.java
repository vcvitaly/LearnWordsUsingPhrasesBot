package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.client.FreeDictionaryApiClient;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionItemDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.FreeDictionaryMeaningsItemDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.FreeDictionaryWordDefinitionResponseItemDto;
import com.github.vcvitaly.learnwordsusingphrases.exception.DefinitionNotFoundException;
import com.github.vcvitaly.learnwordsusingphrases.service.DefinitionApiService;
import lombok.extern.slf4j.Slf4j;
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
public class FreeDictionaryApiService implements DefinitionApiService {

    private FreeDictionaryApiClient client;

    public FreeDictionaryApiService(FreeDictionaryApiClient client) {
        this.client = client;
    }

    @Override
    public List<DefinitionDto> getDefinitions(String word) {
        log.info("Getting a definition for: " + word);
        List<FreeDictionaryWordDefinitionResponseItemDto> wordDefinitionResponses;
        try {
            wordDefinitionResponses = client.getWordDefinitionResponse(word);
        } catch (Exception e) {
            if (e.getCause() instanceof DefinitionNotFoundException) {
                return emptyList();
            }
            throw e;
        }

        return wordDefinitionResponses.get(0).getMeanings().stream()
                .filter(item -> item.getDefinitions().stream()
                        .anyMatch(definitionItem -> Objects.nonNull(definitionItem.getExample()))
                )
                .map(this::toDefinitionDto)
                .toList();
    }

    private DefinitionDto toDefinitionDto(FreeDictionaryMeaningsItemDto meaningsItemDto) {
        var definitionDto = new DefinitionDto();
        definitionDto.setPartOfSpeech(meaningsItemDto.getPartOfSpeech());
        definitionDto.setDefinitionItems(
                meaningsItemDto.getDefinitions().stream()
                        .map(
                                freeDictionaryDefinitionItemDto -> new DefinitionItemDto(
                                        freeDictionaryDefinitionItemDto.getDefinition(),
                                        freeDictionaryDefinitionItemDto.getExample()
                                )
                        ).toList()
        );
        return definitionDto;
    }
}
