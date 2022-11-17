package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vcvitaly.learnwordsusingphrases.client.FreeDictionaryApiClient;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.freedictionary.WordDefinitionResponseItemDto;
import com.github.vcvitaly.learnwordsusingphrases.exception.DefinitionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.github.vcvitaly.learnwordsusingphrases.util.DefinitionPreparationHelper.prepareExpectedDefinitions;
import static com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil.readResourceAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * FreeDictionaryApiServiceTest.
 *
 * @author Vitalii Chura
 */
@ExtendWith(MockitoExtension.class)
class FreeDictionaryApiServiceTest {

    private static final String WORD = "hello";
    private static final String NON_EXISTING_WORD = "aaaaa";

    private FreeDictionaryApiService freeDictionaryApiService;

    @Mock
    private FreeDictionaryApiClient freeDictionaryApiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        freeDictionaryApiService = new FreeDictionaryApiService(freeDictionaryApiClient);
    }

    @Test
    void wordDefinitionsWithExamplesAreReturned() throws JsonProcessingException {
        when(freeDictionaryApiClient.getWordDefinitionResponse(WORD)).thenReturn(prepareResponse());

        var definitions = freeDictionaryApiService.getDefinitions(WORD);

        assertThat(definitions)
                .containsExactlyInAnyOrderElementsOf(
                        prepareExpectedDefinitions(
                                "data/definition_api_service_response/hello_freedictionary_response.json",
                                DefinitionDto[].class
                        )
                );
    }

    @Test
    void emptyListIsReturnedOnException() {
        var exception = new RuntimeException(new DefinitionNotFoundException("some text"));
        when(freeDictionaryApiClient.getWordDefinitionResponse(NON_EXISTING_WORD)).thenThrow(exception);

        var definitions = freeDictionaryApiService.getDefinitions(NON_EXISTING_WORD);

        assertThat(definitions).isEmpty();
    }

    private List<WordDefinitionResponseItemDto> prepareResponse() throws JsonProcessingException {
        var jsonStr = readResourceAsString("__files/hello_freedictionary.json");
        return Arrays.asList(objectMapper.readValue(jsonStr, WordDefinitionResponseItemDto[].class));
    }
}