package com.github.vcvitaly.learnwordsusingphrases;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

/**
 * ITTemplate.
 *
 * @author Vitalii Chura
 */
@ActiveProfiles({"test"})
@SpringBootTest(classes = {LearnWordsUsingPhrasesApplication.class, ITTemplate.MocksConfiguration.class})
public abstract class ITTemplate {

    @TestConfiguration
    public static class MocksConfiguration {
        @MockBean
        private TelegramBotInitializer telegramBotInitializer;

        @MockBean
        private MeterRegistry meterRegistry;
    }
}
