package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.TestUtil;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HtmlGeneratorImplTest.
 *
 * @author Vitalii Chura
 */
class HtmlGeneratorImplTest {

    private HtmlGeneratorImpl htmlGenerator = new HtmlGeneratorImpl();

    @Test
    void htmlIsGenerated() {
        final var s = ResourceUtil.readResourceAsString("data/freemarker/hello_oxford_response.json");
        final var definitionDtos = TestUtil.convertToListOfObjects(s, DefinitionDto.class);

        System.out.println(htmlGenerator.generateHtml(definitionDtos));
    }
}