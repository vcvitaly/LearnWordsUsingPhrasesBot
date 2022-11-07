package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionItemDto;
import com.github.vcvitaly.learnwordsusingphrases.service.DefinitionFormattingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.github.vcvitaly.learnwordsusingphrases.util.Constants.NEW_LINE;
import static com.github.vcvitaly.learnwordsusingphrases.util.MarkdownUtil.getBold;
import static com.github.vcvitaly.learnwordsusingphrases.util.MarkdownUtil.markWordWithBold;

/**
 * MarkdownDefinitionFormattingService.
 *
 * @author Vitalii Chura
 */
@Service
public class MarkdownDefinitionFormattingService implements DefinitionFormattingService {

    @Override
    public String getDefinitionsAsString(List<DefinitionDto> definitions, String word) {
        var definitionSb = new StringBuilder();
        definitionSb.append("Please find definitions for the word ").append(getBold(word)).append(NEW_LINE);
        definitions.stream()
                .map(definition -> getDefinition(definition, word))
                .forEach(definitionSb::append);

        return definitionSb.toString();
    }

    private String getDefinition(DefinitionDto definition, String word) {
        var definitionSb = new StringBuilder();
        definitionSb.append(getBold(definition.getPartOfSpeech())).append(NEW_LINE);
        definition.getDefinitionItems().stream()
                .filter(definitionItem -> Objects.nonNull(definitionItem.getExample()))
                .map(definitionItem -> getDefinition(definitionItem, word))
                .forEach(definitionSb::append);

        return definitionSb.toString();
    }

    private String getDefinition(DefinitionItemDto item, String word) {
        return "Definition: " + item.getDefinition() + NEW_LINE +
                "Example: " + markWordWithBold(item.getExample(), word) + NEW_LINE;
    }
}
