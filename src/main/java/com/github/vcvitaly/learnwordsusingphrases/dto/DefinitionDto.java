package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.Data;

import java.util.List;

/**
 * DefinitionDto.
 *
 * @author Vitalii Chura
 */
@Data
public class DefinitionDto {
    private String partOfSpeech;
    private List<DefinitionItemDto> definitionItems;
}
