package com.github.vcvitaly.learnwordsusingphrases.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.vcvitaly.learnwordsusingphrases.util.Constants.NEW_LINE;

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

    private GoogleDefinitionApiService googleDefinitionApiService;

    public TelegramBotService(GoogleDefinitionApiService googleDefinitionApiService) {
        this.googleDefinitionApiService = googleDefinitionApiService;
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
            }
            if (message.hasText()) {
                String word = message.getText();
                sendText(message.getChatId(), "You sent this word: " + word + NEW_LINE +
                        googleDefinitionApiService.getDefinitionsAsString(word));
            }
        }
    }

    private void sendText(Long chatId, String text){
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString()) //Who are we sending a message to
                .text(text).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}
