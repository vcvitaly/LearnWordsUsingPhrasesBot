package com.github.vcvitaly.learnwordsusingphrases.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TelegramNotificationProperties.
 *
 * @author Vitalii Chura
 */
@ConfigurationProperties("telegram.notification")
public record TelegramNotificationProperties(Boolean enabled, Long chatId) {}
