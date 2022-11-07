package com.github.vcvitaly.learnwordsusingphrases.service;

import org.springframework.stereotype.Service;

/**
 * DefinitionFacadeService.
 *
 * @author Vitalii Chura
 */
@Service
public class DefinitionFacadeService {

    private DefinitionApiService definitionApiService;

    private DefinitionFormattingService definitionFormattingService;

    public DefinitionFacadeService(DefinitionApiService definitionApiService,
                                   DefinitionFormattingService definitionFormattingService) {
        this.definitionApiService = definitionApiService;
        this.definitionFormattingService = definitionFormattingService;
    }

    public String getDefinitionsAsString(String word) {
        var definitions = definitionApiService.getDefinitions(word);
        if (definitions.isEmpty()) {
            return "Definition not found for: " + word;
        }
        return definitionFormattingService.getDefinitionsAsString(definitions, word);
    }
}
