package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.configuration.CacheConfig;
import com.github.vcvitaly.learnwordsusingphrases.configuration.DefinitionServiceList;
import com.github.vcvitaly.learnwordsusingphrases.dto.FormattedDefinitionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * DefinitionFacadeService.
 *
 * @author Vitalii Chura
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefinitionFacadeService {

    private final DefinitionServiceList definitionApiServices;
    private final DefinitionFormattingService definitionFormattingService;

    @Cacheable(CacheConfig.DEFINITION_CACHE)
    public FormattedDefinitionDto getDefinitions(String word) {
        LOG.info("Getting definitions for: " + word);
        for (DefinitionApiService definitionApiService : definitionApiServices) {
            try {
                final var definitions = definitionApiService.getDefinitions(word);
                if (!definitions.isEmpty()) {
                    return FormattedDefinitionDto.builder()
                            .aDefinition(true)
                            .definitionResult(
                                    definitionFormattingService.getDefinitionsAsString(definitions, word)
                            )
                            .build();
                } else {
                    LOG.info("Definitions not found for word '{}' from {}", word, definitionApiService);
                }
            } catch (Exception e) {
                LOG.error("An error happened while fetching definitions via {}", definitionApiService.getClass().getSimpleName(), e);
            }
        }

        LOG.info("Definitions not found for: {}", word);
        return FormattedDefinitionDto.builder()
                .aDefinition(false)
                .definitionResult("Definitions not found for: " + word)
                .build();
    }
}
