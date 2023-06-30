package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.configuration.TelegramBotProperties;
import com.github.vcvitaly.learnwordsusingphrases.configuration.TelegramNotificationProperties;
import com.github.vcvitaly.learnwordsusingphrases.dto.MessageFromDetails;
import com.github.vcvitaly.learnwordsusingphrases.dto.SendMessageDto;
import com.github.vcvitaly.learnwordsusingphrases.enumeration.Command;
import com.github.vcvitaly.learnwordsusingphrases.util.Constants;
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

import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.CLEAR_MY_WORDS;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.DELETE_WORD;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.HELP;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.MY_WORDS;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.SAVE_WORD;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.START;
import static com.github.vcvitaly.learnwordsusingphrases.util.ResourceUtil.readResourceAsString;
import static java.util.Collections.singletonList;

/**
 * TelegramBotService.
 *
 * @author Vitalii Chura
 */
@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private static final String OOPS_TEXT = "Oops, something went wrong.";

    private static final String CALLBACK_DATA_SEPARATOR = "$";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final TelegramBotProperties telegramBotProperties;

    private final DefinitionFacadeService definitionFacadeService;

    private final TelegramMessageChecker messageChecker;

    private final NotificationMessageFormatter notificationMessageFormatter;

    private final DaoFacadeService daoFacadeService;

    private final Counter wordDefRequestCounter;

    private final Counter wordDefProcessedRequestCounter;

    private final TelegramNotificationProperties telegramNotificationProperties;


    public TelegramBotService(TelegramBotProperties telegramBotProperties,
                              DefinitionFacadeService definitionFacadeService,
                              TelegramMessageChecker messageChecker,
                              NotificationMessageFormatter notificationMessageFormatter,
                              DaoFacadeService daoFacadeService,
                              MeterRegistry meterRegistry,
                              TelegramNotificationProperties telegramNotificationProperties) {
        this.telegramBotProperties = telegramBotProperties;
        this.definitionFacadeService = definitionFacadeService;
        this.messageChecker = messageChecker;
        this.notificationMessageFormatter = notificationMessageFormatter;
        this.daoFacadeService = daoFacadeService;
        wordDefRequestCounter = meterRegistry.counter("WordDefinitionRequests");
        wordDefProcessedRequestCounter = meterRegistry.counter("WordDefinitionProcessedRequests");
        this.telegramNotificationProperties = telegramNotificationProperties;
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperties.username();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.token();
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
                notifyAndGetAndSendWordDefinition(message);
            }
        } else if (update.hasCallbackQuery()) {
            final var callbackQuery = update.getCallbackQuery();
            processCallbackQuery(callbackQuery);
        }
    }

    private static void logUpdateReceived(Message message) {
        LOG.info("Received a message [{}] from {}", message.getText(), message.getFrom().getUserName());
    }

    /**
     * Processes a regular command, i.e., not a CRUD one (save_word, delete_word).
     *
     * @param message TelegramBots message object
     */
    private void processRegularCommand(Message message) {
        final var chatId = message.getChatId();
        if (message.getText().equals(START.getCommand())) {
            sendText(chatId, START);
        } else if (message.getText().equals(HELP.getCommand())) {
            sendText(chatId, HELP);
        } else if (message.getText().equals(MY_WORDS.getCommand())) {
            processMyWords(chatId);
        } else if (message.getText().equals(CLEAR_MY_WORDS.getCommand())) {
            processClearMyWords(chatId);
        }
    }

    private void processMyWords(Long chatId) {
        final var userWords = daoFacadeService.getUserWords(chatId);
        if (userWords.isEmpty()) {
            sendSimpleText(chatId, "You have no words saved.");
        } else {
            final var wordsAsString = String.join(Constants.NEW_LINE, userWords);
            sendSimpleText(chatId, wordsAsString);
        }
    }

    private void processClearMyWords(Long chatId) {
        if (daoFacadeService.deleteAllUserWords(chatId)) {
            sendSimpleText(chatId, "All words have been deleted from your list");
        } else {
            sendSimpleText(chatId, "You have no words saved.");
        }
    }

    private void notifyAndGetAndSendWordDefinition(Message message) {
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
        getAndSendWordDefinition(message.getChatId(), definitionRequestText);
    }

    private void getAndSendWordDefinition(Long chatId, String definitionRequestText) {
        SendMessageDto sendMessageDto;
        boolean isSaved;
        try {
            final var formattedDefinitionDto = definitionFacadeService.getDefinitions(definitionRequestText);
            sendMessageDto = SendMessageDto.builder()
                    .aDefinition(formattedDefinitionDto.aDefinition())
                    .message(formattedDefinitionDto.definitionResult())
                    .word(definitionRequestText)
                    .build();
            isSaved = daoFacadeService.hasWordSaved(chatId, definitionRequestText);
        } catch (Exception e) {
            LOG.error(OOPS_TEXT, e);
            sendMessageDto = SendMessageDto.builder()
                    .aDefinition(false)
                    .message(OOPS_TEXT)
                    .build();
            isSaved = false;
        }
        sendText(chatId, sendMessageDto, isSaved);
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        final var message = callbackQuery.getMessage();
        if (message.hasText()) {
            logUpdateReceived(message);
            EditMessageReplyMarkup editMessageReplyMarkup = null;
            final var callbackQueryData = callbackQuery.getData().split("\\" + CALLBACK_DATA_SEPARATOR);
            if (callbackQueryData.length != 2) {
                throw new IllegalStateException("Callback query data should have the format data$word");
            }
            final var data = callbackQueryData[0];
            final var word = callbackQueryData[1];
            final var chatId = message.getChatId();
            if (data.equals(SAVE_WORD.getData())) {
                try {
                    daoFacadeService.saveWordForUser(chatId, word);
                } catch (Exception e) {
                    LOG.error("An error while processing the callback query [{}]", callbackQueryData, e);
                    return;
                }
                editMessageReplyMarkup = getEditMessageReplyMarkup(message, true, word);
            } else if (data.equals(DELETE_WORD.getData())) {
                try {
                    if (daoFacadeService.hasWordSaved(chatId, word)) {
                        daoFacadeService.deleteWordFromUser(chatId, word);
                    }
                } catch (Exception e) {
                    LOG.error("An error while processing the callback query [{}]", callbackQueryData, e);
                    return;
                }
                editMessageReplyMarkup = getEditMessageReplyMarkup(message, false, word);
            }
            try {
                execute(editMessageReplyMarkup);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private EditMessageReplyMarkup getEditMessageReplyMarkup(Message message, boolean isSaved, String word) {
        return EditMessageReplyMarkup.builder()
                .messageId(message.getMessageId())
                .chatId(message.getChatId())
                .replyMarkup(getInlineKeyboardMarkup(isSaved, word))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(boolean isSaved, String word) {
        final var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final var inlineKeyboardButton = InlineKeyboardButton.builder()
                .text(isSaved ? Constants.DELETE_WORD_TEXT : Constants.SAVE_WORD_TEXT)
                .callbackData(isSaved ?
                        getCallbackDataWithSeparator(DELETE_WORD.getData(), word) :
                        getCallbackDataWithSeparator(SAVE_WORD.getData(), word))
                .build();
        inlineKeyboardMarkup.setKeyboard(singletonList(singletonList(inlineKeyboardButton)));
        return inlineKeyboardMarkup;
    }

    private String getCallbackDataWithSeparator(String callbackData, String word) {
        return String.format("%s%s%s", callbackData, CALLBACK_DATA_SEPARATOR, word);
    }

    private void sendText(Long chatId, Command command) {
        sendSimpleText(chatId, readResourceAsString(command.getDescriptionMessageFilePath()));
    }

    private void sendText(Long chatId, SendMessageDto sendMessageDto, boolean isSaved) {
        final var sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageChecker.replaceIllegalChars(sendMessageDto.message()))
                .build();
        sm.enableMarkdownV2(true);
        if (sendMessageDto.aDefinition()) {
            final var inlineKeyboardMarkup = getInlineKeyboardMarkup(isSaved, sendMessageDto.word());
            sm.setReplyMarkup(inlineKeyboardMarkup);
        }
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

    private void sendSimpleText(Long chatId, String text) {
        final var sendMessageDto = SendMessageDto.builder()
                .aDefinition(false)
                .message(text)
                .build();
        sendText(chatId, sendMessageDto, false);
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
