package com.github.vcvitaly.learnwordsusingphrases.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DefinitionFacadeService.
 *
 * @author Vitalii Chura
 */
@Slf4j
@Service
public class DefinitionFacadeService {

    private List<DefinitionApiService> definitionApiServices;

    private DefinitionFormattingService definitionFormattingService;

    public DefinitionFacadeService(List<DefinitionApiService> definitionApiServices,
                                   DefinitionFormattingService definitionFormattingService) {
        this.definitionApiServices = definitionApiServices;
        this.definitionFormattingService = definitionFormattingService;
    }

    public String getDefinitionsAsString(String word) {
        log.info("Getting definitions for: " + word);
        for (DefinitionApiService definitionApiService : definitionApiServices) {
            var definitions = definitionApiService.getDefinitions(word);
            if (!definitions.isEmpty()) {
                return definitionFormattingService.getDefinitionsAsString(definitions, word);
            } else {
                log.info("Definitions not found for word '{}' from {}", word, definitionApiService);
            }
        }

        log.info("Definitions not found for: {}", word);
        return "Definitions not found for: " + word;
    }
}
