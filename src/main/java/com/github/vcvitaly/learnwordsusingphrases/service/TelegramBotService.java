package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.configuration.TelegramNotificationProperties;
import com.github.vcvitaly.learnwordsusingphrases.dto.MessageFromDetails;
import com.github.vcvitaly.learnwordsusingphrases.dto.SendMessageDto;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * TelegramBotService.
 *
 * @author Vitalii Chura
 */
@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String userName;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${telegram.bot.welcome-message}")
    private String welcomeMessage;

    private final DefinitionFacadeService definitionFacadeService;

    private final TelegramMessageChecker messageChecker;

    private final NotificationMessageFormatter notificationMessageFormatter;

    private final Counter wordDefRequestCounter;

    private final Counter wordDefProcessedRequestCounter;

    private final TelegramNotificationProperties telegramNotificationProperties;


    public TelegramBotService(DefinitionFacadeService definitionFacadeService,
                              TelegramMessageChecker messageChecker,
                              NotificationMessageFormatter notificationMessageFormatter,
                              MeterRegistry meterRegistry,
                              TelegramNotificationProperties telegramNotificationProperties) {
        this.definitionFacadeService = definitionFacadeService;
        this.messageChecker = messageChecker;
        this.notificationMessageFormatter = notificationMessageFormatter;
        wordDefRequestCounter = meterRegistry.counter("WordDefinitionRequests");
        wordDefProcessedRequestCounter = meterRegistry.counter("WordDefinitionProcessedRequests");
        this.telegramNotificationProperties = telegramNotificationProperties;
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOG.debug("Received an update: {}", update);
        if (update.hasMessage()) {
            final var message = update.getMessage();
            if (message.isCommand()) {
                if (message.getText().equals("/start")) {
                    final var sendMessageDto = SendMessageDto.builder()
                            .aDefinition(false)
                            .message(welcomeMessage)
                            .build();
                    sendText(message.getChatId(),  sendMessageDto);
                }
                return;
            }
            if (message.hasText()) {
                final var definitionRequestText = message.getText();
                incrementCounter(wordDefRequestCounter);
                String notificationMessage = null;
                try {
                    notificationMessage = notificationMessageFormatter.formatMessage(
                            MessageFromDetails.builder()
                                    .username(message.getFrom().getUserName())
                                    .firstName(message.getFrom().getFirstName())
                                    .lastName(message.getFrom().getLastName())
                                    .build(),
                            definitionRequestText
                    );
                    sendNotificationToMonitoringGroup(notificationMessage);
                } catch (Exception e) {
                    LOG.error("Could not send a notification message [{}] to the monitoring chat", notificationMessage, e);
                }
                String replyText;
                SendMessageDto sendMessageDto;
                try {
                    replyText = definitionFacadeService.getDefinitionsAsString(definitionRequestText);
                    sendMessageDto = SendMessageDto.builder()
                            .aDefinition(true)
                            .message(replyText)
                            .build();
                } catch (Exception e) {
                    LOG.error("Oops, something went wrong.", e);
                    replyText = "Oops, something went wrong.";
                    sendMessageDto = SendMessageDto.builder()
                            .aDefinition(false)
                            .message(replyText)
                            .build();
                }
                sendText(message.getChatId(),  sendMessageDto);
            }
        }
    }

    private void sendText(Long chatId, SendMessageDto sendMessageDto) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageChecker.replaceIllegalChars(sendMessageDto.message()))
                .build();
        sm.enableMarkdownV2(true);
        try {
            execute(sm);
            if (sendMessageDto.aDefinition()) {
                incrementCounter(wordDefProcessedRequestCounter);
            }
            LOG.info(
                    "Replied with {} to {}",
                    sendMessageDto.aDefinition() ? "definition" : sendMessageDto.message(),
                    chatId
            );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void incrementCounter(Counter counter) {
        if (activeProfile.equals("prod")) {
            counter.increment();
        }
    }

    private void sendNotificationToMonitoringGroup(String message) {
        if (telegramNotificationProperties.enabled()) {
            final var sendMessageDto = SendMessageDto.builder()
                    .aDefinition(false)
                    .message(message)
                    .build();
            sendText(telegramNotificationProperties.chatId(), sendMessageDto);
        }
    }
}
