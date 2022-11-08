package com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary;

import java.util.List;
import lombok.Data;

@Data
public class WordDefinitionResponseItemDto {
    private LicenseDto license;
    private String phonetic;
    private List<PhoneticsItemDto> phonetics;
    private String word;
    private List<MeaningsItemDto> meanings;
    private List<String> sourceUrls;
}