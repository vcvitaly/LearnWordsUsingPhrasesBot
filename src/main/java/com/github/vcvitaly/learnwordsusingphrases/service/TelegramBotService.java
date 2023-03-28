package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.enumeration.Command;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.HELP;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.START;
import static com.github.vcvitaly.learnwordsusingphrases.enumeration.Command.SUBSCRIBED_WORDS;
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

    private final Counter wordDefRequestCounter;

    private final Counter wordDefProcessedRequestCounter;


    public TelegramBotService(DefinitionFacadeService definitionFacadeService,
                              TelegramMessageChecker messageChecker,
                              MeterRegistry meterRegistry) {
        this.definitionFacadeService = definitionFacadeService;
        this.messageChecker = messageChecker;
        wordDefRequestCounter = meterRegistry.counter("WordDefinitionRequests");
        wordDefProcessedRequestCounter = meterRegistry.counter("WordDefinitionProcessedRequests");
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
        log.debug("Received an update: {}", update);
        if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasText()) {
                log.info("Received a message {} from {}", message.getText(), message.getFrom().getUserName());
            }
            if (message.isCommand()) {
                if (message.getText().equals(START.getCommand())) {
                    sendText(message.getChatId(), START);
                } else if (message.getText().equals(HELP.getCommand())) {
                    sendText(message.getChatId(), HELP);
                } else if (message.getText().equals(SUBSCRIBED_WORDS.getCommand())) {
                    sendText(message.getChatId(), "Not implemented yet");
                }
            } else if (message.hasText()) {
                incrementCounter(wordDefRequestCounter);
                String word = message.getText();
                String text;
                try {
                    text = definitionFacadeService.getDefinitionsAsString(word);
                } catch (Exception e) {
                    log.error("Oops, something went wrong.", e);
                    text = "Oops, something went wrong.";
                }
                sendText(message.getChatId(), text);
            }
        }
    }

    private void sendText(Long chatId, Command command) {
        sendText(chatId, readResourceAsString(command.getDescriptionFilePath()));
    }

    private void sendText(Long chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageChecker.replaceIllegalChars(text))
                .build();
        sm.enableMarkdownV2(true);
        try {
            execute(sm);
            incrementCounter(wordDefProcessedRequestCounter);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void incrementCounter(Counter counter) {
        if (activeProfile.equals("prod")) {
            counter.increment();
        }
    }
}
