package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.configuration.DefinitionServiceList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public String getDefinitionsAsString(String word) {
        LOG.info("Getting definitions for: " + word);
        for (DefinitionApiService definitionApiService : definitionApiServices) {
            try {
                final var definitions = definitionApiService.getDefinitions(word);
                if (!definitions.isEmpty()) {
                    return definitionFormattingService.getDefinitionsAsString(definitions, word);
                } else {
                    LOG.info("Definitions not found for word '{}' from {}", word, definitionApiService);
                }
            } catch (Exception e) {
                LOG.error("An error happened while fetching definitions via {}", definitionApiService.getClass().getSimpleName(), e);
            }
        }

        LOG.info("Definitions not found for: {}", word);
        return "Definitions not found for: " + word;
    }
}
