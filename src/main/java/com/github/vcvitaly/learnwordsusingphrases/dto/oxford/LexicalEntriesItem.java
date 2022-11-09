package com.github.vcvitaly.learnwordsusingphrases.dto.oxford;

import java.util.List;
import lombok.Data;

@Data
public class LexicalEntriesItem {
    private List<EntriesItem> entries;
    private LexicalCategory lexicalCategory;
    private String language;
    private String text;
}