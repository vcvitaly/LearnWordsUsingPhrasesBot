package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.vcvitaly.learnwordsusingphrases.client.FreeDictionaryApiClient.ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefinitionFacadeServiceIntegrationTest.
 *
 * @author Vitalii Chura
 */
@ActiveProfiles({"test"})
@SpringBootTest
class DefinitionFacadeServiceIntegrationTest {

    private static final String WORD = "hello";
    private static final String NON_EXISTING_WORD = "aaaaa";

    @MockBean
    private TelegramBotInitializer telegramBotInitializer;

    @Autowired
    private DefinitionFacadeService definitionFacadeService;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("route.freedictionaryapi", wireMockServer::baseUrl);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
    }

    @Test
    void wordDefinitionsWithExamplesAreReturned() {
        wireMockServer.stubFor(
                get(ENDPOINT + WORD)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("hello.json")
                                        .withStatus(200)
                        )
        );
        var expectedString = """
                Please find definitions for the word *hello*
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

        assertThat(definitionFacadeService.getDefinitionsAsString(WORD))
                .isEqualTo(expectedString);
    }

    @Test
    void definitionNotFoundIsReturnedOnIncorrectWord() {
        wireMockServer.stubFor(
                get(ENDPOINT + NON_EXISTING_WORD)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("definition_not_found.json")
                                        .withStatus(404)
                        )
        );

        var expectedString = String.format("Definition not found for: %s", NON_EXISTING_WORD);

        assertThat(definitionFacadeService.getDefinitionsAsString(NON_EXISTING_WORD))
                .isEqualTo(expectedString);
    }
}