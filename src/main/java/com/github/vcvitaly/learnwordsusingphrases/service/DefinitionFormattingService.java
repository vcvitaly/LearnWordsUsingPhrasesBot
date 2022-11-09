package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;

import java.util.List;

/**
 * DefinitionFormattingService.
 *
 * @author Vitalii Chura
 */
public interface DefinitionFormattingService {

    String getDefinitionsAsString(List<DefinitionDto> definitions, String word);
}
