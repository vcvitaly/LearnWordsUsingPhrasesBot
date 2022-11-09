package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DefinitionItemDto.
 *
 * @author Vitalii Chura
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefinitionItemDto {
    private String definition;
    private String example;
}
