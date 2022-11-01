package com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary;

import java.util.List;
import lombok.Data;

@Data
public class WordDefinitionResponseItem {
    private License license;
    private String phonetic;
    private List<PhoneticsItem> phonetics;
    private String word;
    private List<MeaningsItem> meanings;
    private List<String> sourceUrls;
}