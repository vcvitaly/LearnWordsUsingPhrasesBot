package com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary;

import java.util.List;
import lombok.Data;

@Data
public class FreeDictionaryMeaningsItemDto {
    private List<String> synonyms;
    private String partOfSpeech;
    private List<String> antonyms;
    private List<FreeDictionaryDefinitionItemDto> definitions;
}