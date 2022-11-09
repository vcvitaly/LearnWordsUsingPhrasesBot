package com.github.vcvitaly.learnwordsusingphrases.dto.oxford;

import java.util.List;
import lombok.Data;

@Data
public class SubsensesItem {
    private List<ExamplesItem> examples;
    private String id;
    private List<String> definitions;
}