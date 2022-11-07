package com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary;

import java.util.List;
import lombok.Data;

@Data
public class FreeDictionaryWordDefinitionResponseItemDto {
    private FreeDictionaryLicenseDto license;
    private String phonetic;
    private List<FreeDictionaryPhoneticsItemDto> phonetics;
    private String word;
    private List<FreeDictionaryMeaningsItemDto> meanings;
    private List<String> sourceUrls;
}