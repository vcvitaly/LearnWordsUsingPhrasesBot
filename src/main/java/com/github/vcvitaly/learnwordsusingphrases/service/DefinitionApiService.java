package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;

import java.util.List;

/**
 * DefinitionApiService.
 *
 * @author Vitalii Chura
 */
public interface DefinitionApiService {

    List<DefinitionDto> getDefinitions(String word);
}
