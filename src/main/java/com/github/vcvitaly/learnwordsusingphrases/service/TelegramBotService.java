package com.github.vcvitaly.learnwordsusingphrases.service;

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

    private DefinitionFacadeService definitionFacadeService;

    private TelegramMessageChecker messageChecker;

    public TelegramBotService(DefinitionFacadeService definitionFacadeService,
                              TelegramMessageChecker messageChecker) {
        this.definitionFacadeService = definitionFacadeService;
        this.messageChecker = messageChecker;
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
            if (message.isCommand()) {
                if (message.getText().equals("/start")) {
                    sendText(message.getChatId(), "Please type a word and I'll send you it's definition and " +
                            "an example of it's usage.");
                }
                return;
            }
            if (message.hasText()) {
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

    private void sendText(Long chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageChecker.replaceIllegalChars(text))
                .build();
        sm.enableMarkdownV2(true);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
