package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.Data;

import java.util.List;

/**
 * DefinitionDto - a list of such definitions is provided by DefinitionApiService, which implementations query the
 * appropriate API and convert the result into the above list.
 *
 * @author Vitalii Chura
 */
@Data
public class DefinitionDto {
    private String partOfSpeech;
    private List<DefinitionItemDto> definitionItems;
}
