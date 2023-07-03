package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.TestUtil;
import com.github.vcvitaly.learnwordsusingphrases.configuration.FreemarkerConfig;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * HtmlGeneratorImplTest.
 *
 * @author Vitalii Chura
 */
@SpringBootTest(classes = {FreemarkerConfig.class, HtmlGeneratorImpl.class})
class HtmlGeneratorImplTest {

    @Autowired
    private HtmlGeneratorImpl htmlGenerator;

    @Test
    void htmlIsGenerated() {
        final var s = ResourceUtil.readResourceAsString("data/freemarker/hello_oxford_response.json");
        final var definitionDtos = TestUtil.convertToListOfObjects(s, DefinitionDto.class);

//        System.out.println(htmlGenerator.generateHtml(definitionDtos));
        String html = htmlGenerator.generateHtml(definitionDtos);

        assertThat(html).isNotEmpty();
    }
}