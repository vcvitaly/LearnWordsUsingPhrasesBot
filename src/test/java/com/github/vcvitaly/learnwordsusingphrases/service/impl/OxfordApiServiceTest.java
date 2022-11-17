package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vcvitaly.learnwordsusingphrases.client.OxfordApiClient;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.dto.oxford.Response;
import com.github.vcvitaly.learnwordsusingphrases.exception.DefinitionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.github.vcvitaly.learnwordsusingphrases.enumeration.OxfordApiSourceLang.EN_GB;
import static com.github.vcvitaly.learnwordsusingphrases.service.impl.OxfordApiService.FIELDS;
import static com.github.vcvitaly.learnwordsusingphrases.util.DefinitionPreparationHelper.prepareExpectedDefinitions;
import static com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil.readResourceAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * OxfordApiServiceTest.
 *
 * @author Vitalii Chura
 */
@ExtendWith(MockitoExtension.class)
class OxfordApiServiceTest {

    private static final String WORD = "hello";
    private static final String NON_EXISTING_WORD = "aaaaa";

    private OxfordApiService oxfordApiService;

    @Mock
    private OxfordApiClient oxfordApiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        oxfordApiService = new OxfordApiService(oxfordApiClient);
    }

    @Test
    void wordDefinitionsWithExamplesAreReturned() throws JsonProcessingException {
        when(oxfordApiClient.getWordDefinitionResponse(
                null, null, EN_GB.getSourceLang(), WORD, FIELDS, false)
        ).thenReturn(prepareResponse("__files/hello_oxford.json"));

        var definitions = oxfordApiService.getDefinitions(WORD);

        assertThat(definitions)
                .containsExactlyInAnyOrderElementsOf(
                        prepareExpectedDefinitions(
                                "data/definition_api_service_response/hello_oxford_response.json",
                                DefinitionDto[].class
                        )
                );
    }

    @Test
    void anEmptyDefinitionListIsReturnedForAWordWithNoExamples() throws JsonProcessingException {
        when(oxfordApiClient.getWordDefinitionResponse(
                null, null, EN_GB.getSourceLang(), WORD, FIELDS, false)
        ).thenReturn(prepareResponse("data/api_response/apple_oxford.json"));

        var definitions = oxfordApiService.getDefinitions(WORD);

        assertThat(definitions).isEmpty();
    }

    @Test
    void emptyListIsReturnedOnException() {
        var exception = new RuntimeException(new DefinitionNotFoundException("some text"));
        when(oxfordApiClient.getWordDefinitionResponse(
                null, null, EN_GB.getSourceLang(), NON_EXISTING_WORD, FIELDS, false)
        ).thenThrow(exception);

        var definitions = oxfordApiService.getDefinitions(NON_EXISTING_WORD);

        assertThat(definitions).isEmpty();
    }

    private Response prepareResponse(String resource) throws JsonProcessingException {
        var jsonStr = readResourceAsString(resource);
        return objectMapper.readValue(jsonStr, Response.class);
    }
}
