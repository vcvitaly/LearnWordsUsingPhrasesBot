package com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary;

import lombok.Data;

@Data
public class PhoneticsItem {
    private String text;
    private String audio;
    private String sourceUrl;
}