package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.client.OxfordApiClient;
import com.github.vcvitaly.learnwordsusingphrases.configuration.OxfordApiProperties;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionItemDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.oxford.EntriesItem;
import com.github.vcvitaly.learnwordsusingphrases.dto.oxford.LexicalEntriesItem;
import com.github.vcvitaly.learnwordsusingphrases.dto.oxford.Response;
import com.github.vcvitaly.learnwordsusingphrases.dto.oxford.ResultsItem;
import com.github.vcvitaly.learnwordsusingphrases.dto.oxford.SensesItem;
import com.github.vcvitaly.learnwordsusingphrases.exception.DefinitionNotFoundException;
import com.github.vcvitaly.learnwordsusingphrases.service.DefinitionApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.github.vcvitaly.learnwordsusingphrases.enumeration.OxfordApiSourceLang.EN_GB;
import static java.util.Collections.emptyList;

/**
 * OxfordApiService.
 *
 * @author Vitalii Chura
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OxfordApiService implements DefinitionApiService {

    static final String[] FIELDS = new String[] {"definitions", "examples"};

    private final OxfordApiProperties oxfordApiProperties;

    private final OxfordApiClient oxfordApiClient;

    @Override
    public List<DefinitionDto> getDefinitions(String word) {
        Response wordDefinitionResponse;
        try {
             wordDefinitionResponse = oxfordApiClient.getWordDefinitionResponse(
                    oxfordApiProperties.appId(), oxfordApiProperties.appKey(), EN_GB.getSourceLang(), word, FIELDS, false
             );
        } catch (Exception e) {
            if (e.getCause() instanceof DefinitionNotFoundException) {
                return emptyList();
            }
            throw e;
        }

        LOG.debug("Received a response for word '{}' from oxford api: {}", word, wordDefinitionResponse);
        return wordDefinitionResponse.getResults().stream()
                .map(ResultsItem::getLexicalEntries)
                .flatMap(Collection::stream)
                .filter(this::hasAtLeastOneEntryWithSenseAndExamples)
                .map(this::toDefinitionDto)
                .toList();
    }

    private boolean hasAtLeastOneEntryWithSenseAndExamples(LexicalEntriesItem lexicalEntriesItem) {
        return !lexicalEntriesItem.getEntries().isEmpty() &&
                lexicalEntriesItem.getEntries().get(0).getSenses() != null &&
                lexicalEntriesItem.getEntries().get(0).getSenses().stream()
                        .anyMatch(sensesItem -> sensesItem.getExamples() != null);
    }

    private DefinitionDto toDefinitionDto(LexicalEntriesItem lexicalEntriesItem) {
        final var definitionDto = new DefinitionDto();
        definitionDto.setPartOfSpeech(lexicalEntriesItem.getLexicalCategory().getId());
        final var definitionItems = lexicalEntriesItem.getEntries().stream()
                .map(EntriesItem::getSenses)
                .flatMap(Collection::stream)
                .filter(sensesItem -> sensesItem.getExamples() != null)
                .map(this::toDefinitionItemDto)
                .toList();
        definitionDto.setDefinitionItems(definitionItems);

        return definitionDto;
    }

    private DefinitionItemDto toDefinitionItemDto(SensesItem sensesItem) {
        return new DefinitionItemDto(
                sensesItem.getDefinitions().get(0),
                sensesItem.getExamples().get(0).getText()
        );
    }
}
