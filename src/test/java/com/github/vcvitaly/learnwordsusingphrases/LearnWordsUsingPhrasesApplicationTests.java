package com.github.vcvitaly.learnwordsusingphrases;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

@ActiveProfiles("test")
@SpringBootTest
class LearnWordsUsingPhrasesApplicationTests {

	@MockBean
	private TelegramBotInitializer telegramBotInitializer;

	@Test
	void contextLoads() {
	}

}
