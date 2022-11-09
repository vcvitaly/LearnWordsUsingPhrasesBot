package com.github.vcvitaly.learnwordsusingphrases.dto.oxford;

import java.util.List;
import lombok.Data;

@Data
public class SensesItem {
    private List<ExamplesItem> examples;
    private String id;
    private List<String> definitions;
    private List<SubsensesItem> subsenses;
}