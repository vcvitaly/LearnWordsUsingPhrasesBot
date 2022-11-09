package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil.readResourceAsString;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * MarkdownDefinitionFormattingServiceTest.
 *
 * @author Vitalii Chura
 */
class MarkdownDefinitionFormattingServiceTest {

    private static final String WORD = "hello";
    private MarkdownDefinitionFormattingService formattingService = new MarkdownDefinitionFormattingService();

    @Test
    void responseObjectIsFormattedAsStr() throws JsonProcessingException {
        List<DefinitionDto> definitions = prepareDefinitions();

        var actualDefinitionStr = formattingService.getDefinitionsAsString(definitions, WORD);

        var expectedString = """
                Please find definitions for the word *hello*:
                *interjection*
                Definition: A greeting (salutation) said when meeting someone or acknowledging someone’s arrival or presence.
                Example: *Hello*, everyone.
                Definition: A greeting used when answering the telephone.
                Example: *Hello*? How may I help you?
                Definition: A call for response if it is not clear if anyone is present or listening, or if a telephone conversation may have been disconnected.
                Example: *Hello*? Is anyone there?
                Definition: Used sarcastically to imply that the person addressed or referred to has done something the speaker or writer considers to be foolish.
                Example: You just tried to start your car with your cell phone. *Hello*?
                Definition: An expression of puzzlement or discovery.
                Example: *Hello*! What’s going on here?
                 """;
        assertThat(actualDefinitionStr).isEqualTo(expectedString);
    }

    private List<DefinitionDto> prepareDefinitions() throws JsonProcessingException {
        var jsonStr = readResourceAsString("data/definition_api_service_response/hello_freedictionary_response.json");
        var objectMapper = new ObjectMapper();
        return Arrays.asList(objectMapper.readValue(jsonStr, DefinitionDto[].class));
    }
}
