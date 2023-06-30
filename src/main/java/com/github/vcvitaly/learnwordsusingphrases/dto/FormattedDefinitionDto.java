package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.Builder;

/**
 * FormattedDefinitionDto.
 *
 * @author Vitalii Chura
 */
@Builder
public record FormattedDefinitionDto(boolean aDefinition, String definitionResult) {}
