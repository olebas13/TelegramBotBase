package com.olebas.telegrambotbase.service;

import com.olebas.telegrambotbase.bot.Bot;
import com.olebas.telegrambotbase.command.ParsedCommand;
import com.olebas.telegrambotbase.command.Parser;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageReceiver implements Runnable {

    private static final Logger log = Logger.getLogger(MessageReceiver.class);
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    private Bot bot;
    private Parser parser;

    public MessageReceiver(Bot bot) {
        this.bot = bot;
        parser = new Parser(bot.getBotUsername());
    }

    @Override
    public void run() {
        log.info("[STARTED] MsgReceiver. Bot class: " + bot);

        try {
            while (true) {
                for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                    log.debug("New object for analyze in queue " + object.toString());
                    analyze(object);
                }

                try {
                    Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
                } catch (InterruptedException e) {
                    log.error("Catch interrupt. Exit", e);
                    return;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void analyze(Object object) {
        if (object instanceof Update) {
            Update update = (Update) object;
            log.debug("Update received: " + update.toString());
            analyzeForUpdateType(update);
        } else {
            log.warn("Can't operate type of object: " + object.toString());
        }
    }

    private void analyzeForUpdateType(Update update) {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        ParsedCommand parsedCommand = parser.getParsedCommand(inputText);
    }
}
