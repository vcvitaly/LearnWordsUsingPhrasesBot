package com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary;

import java.util.List;
import lombok.Data;

@Data
public class MeaningsItemDto {
    private List<String> synonyms;
    private String partOfSpeech;
    private List<String> antonyms;
    private List<DefinitionItemDto> definitions;
}