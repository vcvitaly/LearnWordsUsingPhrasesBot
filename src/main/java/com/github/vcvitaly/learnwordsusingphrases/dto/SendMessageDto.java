package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.Builder;
import lombok.Data;

/**
 * SendMessageDto.
 *
 * @author Vitalii Chura
 */
@Data
@Builder
public class SendMessageDto {

    private boolean aDefinition;
    private String message;
}
