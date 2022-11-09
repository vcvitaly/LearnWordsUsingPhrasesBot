package com.github.vcvitaly.learnwordsusingphrases.dto.oxford;

import java.util.List;
import lombok.Data;

@Data
public class ResultsItem {
    private List<LexicalEntriesItem> lexicalEntries;
    private String language;
    private String id;
    private String type;
    private String word;
}