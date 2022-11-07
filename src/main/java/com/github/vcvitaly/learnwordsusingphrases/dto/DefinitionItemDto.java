package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DefinitionItemDto.
 *
 * @author Vitalii Chura
 */
@Data
@AllArgsConstructor
public class DefinitionItemDto {
    private String definition;
    private String example;
}
