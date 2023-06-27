package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.Builder;
import lombok.Data;

/**
 * SendMessageDto.
 *
 * @author Vitalii Chura
 */
@Builder
public record SendMessageDto(boolean aDefinition, String message) {}
