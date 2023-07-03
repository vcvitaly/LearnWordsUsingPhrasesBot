package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;

import java.util.List;

/**
 * HtmlGenerator.
 *
 * @author Vitalii Chura
 */
public interface HtmlGenerator {

    String generateHtml(List<DefinitionDto> definitionDtos);
}
