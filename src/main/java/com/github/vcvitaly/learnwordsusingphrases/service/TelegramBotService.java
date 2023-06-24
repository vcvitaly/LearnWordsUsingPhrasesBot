package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.enumeration.Command;
import com.github.vcvitaly.learnwordsusingphrases.util.Constants;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;

import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.HELP;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.START;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.SUBSCRIBE;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.SUBSCRIBED_WORDS;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.UNSUBSCRIBE;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.UNSUBSCRIBE_ALL;
import static com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil.readResourceAsString;

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
            if (message.isCommand() || message.hasText()) {
                logUpdateReceived(message);
            }
            if (message.isCommand()) {
                processRegularCommand(message);
            } else if (message.hasText()) { // if is a request for a word definition
                getAndSendWordDefinition(message);
            }
        } else if (update.hasCallbackQuery()) {
            final var callbackQuery = update.getCallbackQuery();
            processCallbackQuery(callbackQuery);
        }
    }

    private static void logUpdateReceived(Message message) {
        LOG.info("Received a message [{}] from {}", message.getText(), message.getFrom().getUserName());
    }

    private void processRegularCommand(Message message) {
        if (message.getText().equals(START.getCommand())) {
            sendText(message.getChatId(), START);
        } else if (message.getText().equals(HELP.getCommand())) {
            sendText(message.getChatId(), HELP);
        } else if (message.getText().equals(SUBSCRIBED_WORDS.getCommand())) {
            sendText(message.getChatId(), "Not implemented yet");
        } else if (message.getText().equals(UNSUBSCRIBE_ALL.getCommand())) {
            sendText(message.getChatId(), "Not implemented yet");
        }
    }

    private void getAndSendWordDefinition(Message message) {
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
        boolean isSubscribed;
        try {
            replyText = definitionFacadeService.getDefinitionsAsString(definitionRequestText);
            sendMessageDto = SendMessageDto.builder()
                    .aDefinition(true)
                    .message(replyText)
                    .build();
            isSubscribed = false; // TODO get a isSubscribed from the DefinitionFacadeService
        } catch (Exception e) {
            LOG.error("Oops, something went wrong.", e);
            replyText = "Oops, something went wrong.";
            sendMessageDto = SendMessageDto.builder()
                    .aDefinition(false)
                    .message(replyText)
                    .build();
            isSubscribed = false;
        }
        sendText(message.getChatId(), sendMessageDto, isSubscribed);
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        final var message = callbackQuery.getMessage();
        if (message.hasText()) {
            logUpdateReceived(message);
            EditMessageReplyMarkup editMessageReplyMarkup = null;
            if (callbackQuery.getData().equals(SUBSCRIBE.getData())) {
                editMessageReplyMarkup = getEditMessageReplyMarkup(message, true);
                LOG.debug("sub"); // TODO implement
            } else if (callbackQuery.getData().equals(UNSUBSCRIBE.getData())) {
                editMessageReplyMarkup = getEditMessageReplyMarkup(message, false);
                LOG.debug("unsub"); // TODO implement
            }
            try {
                execute(editMessageReplyMarkup);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private EditMessageReplyMarkup getEditMessageReplyMarkup(Message message, boolean isSubscribed) {
        return EditMessageReplyMarkup.builder()
                .messageId(message.getMessageId())
                .chatId(message.getChatId())
                .replyMarkup(getInlineKeyboardMarkup(isSubscribed))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(boolean isSubscribed) {
        final var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final var inlineKeyboardButton = InlineKeyboardButton.builder()
                .text(isSubscribed ? Constants.UNSUBSCRIBE_TEXT : Constants.SUBSCRIBE_TEXT)
                .callbackData(isSubscribed ? UNSUBSCRIBE.getData() : SUBSCRIBE.getData())
                .build();
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(Collections.singletonList(inlineKeyboardButton)));
        return inlineKeyboardMarkup;
    }

    private void sendText(Long chatId, Command command) {
        sendText(chatId, readResourceAsString(command.getDescriptionFilePath()));
    }

    private void sendText(Long chatId, String text) {
        final var sendMessageDto = SendMessageDto.builder()
                .aDefinition(false)
                .message(text)
                .build();
        sendText(chatId, sendMessageDto, false);
    }

    private void sendText(Long chatId, SendMessageDto sendMessageDto, boolean isSubscribed) {
        final var sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageChecker.replaceIllegalChars(sendMessageDto.message()))
                .build();
        sm.enableMarkdownV2(true);
        if (sendMessageDto.aDefinition()) {
            final var inlineKeyboardMarkup = getInlineKeyboardMarkup(isSubscribed);
            sm.setReplyMarkup(inlineKeyboardMarkup);
        }
        try {
            execute(sm);
            if (sendMessageDto.aDefinition()) {
                incrementCounter(wordDefProcessedRequestCounter);
            }
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
            sendText(telegramNotificationProperties.chatId(), sendMessageDto, false);
        }
    }
}
