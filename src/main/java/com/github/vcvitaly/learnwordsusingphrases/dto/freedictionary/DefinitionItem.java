package com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary;

import java.util.List;
import lombok.Data;

@Data
public class DefinitionItem {
    private List<String> synonyms;
    private List<String> antonyms;
    private String definition;
    private String example;
}