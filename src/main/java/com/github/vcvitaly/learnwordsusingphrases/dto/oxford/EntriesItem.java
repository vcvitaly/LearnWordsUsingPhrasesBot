package com.github.vcvitaly.learnwordsusingphrases.dto.oxford;

import java.util.List;
import lombok.Data;

@Data
public class EntriesItem {
    private String homographNumber;
    private List<SensesItem> senses;
}