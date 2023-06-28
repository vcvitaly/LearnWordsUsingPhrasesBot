package com.github.vcvitaly.learnwordsusingphrases.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TelegramNotificationProperties.
 *
 * @author Vitalii Chura
 */
@ConfigurationProperties("telegram.bot")
public record TelegramBotProperties(String username, String token) {}
