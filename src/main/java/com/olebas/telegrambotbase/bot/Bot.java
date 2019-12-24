package com.olebas.telegrambotbase.bot;

import com.olebas.telegrambotbase.util.ReadProperties;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {

    private static final Logger log = Logger.getLogger(Bot.class);
    private static final ReadProperties prop = new ReadProperties();

    final int RECONNECT_PAUSE = 10000;

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new update. UpdateID: " + update.getUpdateId());

        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        if (inputText.startsWith("/start")) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Hello. This is a start message");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return prop.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return prop.getBotToken();
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            log.error("Can't connect. Pause " + RECONNECT_PAUSE / 1000 + " sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                return;
            }
            botConnect();
        }
    }
}
