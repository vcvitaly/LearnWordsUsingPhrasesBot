package com.github.vcvitaly.learnwordsusingphrases;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

/**
 * ITTemplate.
 *
 * @author Vitalii Chura
 */
@ActiveProfiles({"test"})
@SpringBootTest
public abstract class ITTemplate {

    @MockBean
    private TelegramBotInitializer telegramBotInitializer;

    @MockBean
    private MeterRegistry meterRegistry;
}
