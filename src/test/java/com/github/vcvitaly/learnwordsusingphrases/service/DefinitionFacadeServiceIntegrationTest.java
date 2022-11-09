package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.vcvitaly.learnwordsusingphrases.client.FreeDictionaryApiClient;
import com.github.vcvitaly.learnwordsusingphrases.client.OxfordApiClient;
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
import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefinitionFacadeServiceIntegrationTest.
 *
 * @author Vitalii Chura
 */
@ActiveProfiles({"test"})
@SpringBootTest
class DefinitionFacadeServiceIntegrationTest {

    private static final String WORD_HELLO = "hello";
    private static final String WORD_LURE = "lure";
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
        registry.add("route.oxfordapi", wireMockServer::baseUrl);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
    }

    @Test
    void wordDefinitionsAreReturnedFromFreeDictionaryApi() {
        wireMockServer.stubFor(
                get(FreeDictionaryApiClient.ENDPOINT + WORD_HELLO)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("hello_freedictionary.json")
                                        .withStatus(200)
                        )
        );

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
        var definitionsAsString = definitionFacadeService.getDefinitionsAsString(WORD_HELLO);
        assertThat(definitionsAsString).isEqualTo(expectedString);
    }

    @Test
    void definitionNotFoundIsReturnedOnIncorrectWord() {
        wireMockServer.stubFor(
                get(FreeDictionaryApiClient.ENDPOINT + NON_EXISTING_WORD)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("definition_not_found.json")
                                        .withStatus(404)
                        )
        );

        var expectedString = String.format("Definitions not found for: %s", NON_EXISTING_WORD);

        assertThat(definitionFacadeService.getDefinitionsAsString(NON_EXISTING_WORD))
                .isEqualTo(expectedString);
    }

    @Test
    void wordDefinitionsAreReturnedFromOxfordApi() {
        wireMockServer.stubFor(
                get(OxfordApiClient.ENDPOINT + "/entries/en-gb/" + WORD_HELLO + "?fields=definitions%2Cexamples&strictMatch=false")
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("hello_oxford.json")
                                        .withStatus(200)
                        )
        );

        var expectedString = """
                Please find definitions for the word *hello*:
                *interjection*
                Definition: used as a greeting or to begin a phone conversation
                Example: *hello* there, Katie!
                *noun*
                Definition: an utterance of ‘hello’; a greeting
                Example: she was getting polite nods and *hello*s from people
                *verb*
                Definition: say or shout ‘hello’
                Example: I pressed the phone button and *hello*ed
                 """;
        var definitionsAsString = definitionFacadeService.getDefinitionsAsString(WORD_HELLO);
        assertThat(definitionsAsString).isEqualTo(expectedString);
    }

    @Test
    void wordDefinitionsAreReturnedFromOxfordApiForAWordWithMissingExamples() {
        wireMockServer.stubFor(
                get(OxfordApiClient.ENDPOINT + "/entries/en-gb/" + WORD_LURE + "?fields=definitions%2Cexamples&strictMatch=false")
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("lure_oxford.json")
                                        .withStatus(200)
                        )
        );

        var expectedString = """
                Please find definitions for the word *lure*:
                *verb*
                Definition: tempt (a person or animal) to do something or to go somewhere, especially by offering some form of reward
                Example: the child was *lure*d into a car but managed to escape
                *noun*
                Definition: something that tempts or is used to tempt a person or animal to do something
                Example: the film industry always has been a glamorous *lure* for young girls
                 """;
        var definitionsAsString = definitionFacadeService.getDefinitionsAsString(WORD_LURE);
        assertThat(definitionsAsString).isEqualTo(expectedString);
    }
}