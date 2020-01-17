package com.olebas.telegrambotbase.service;

import com.olebas.telegrambotbase.bot.Bot;
import com.olebas.telegrambotbase.command.Command;
import com.olebas.telegrambotbase.command.ParsedCommand;
import com.olebas.telegrambotbase.command.Parser;
import com.olebas.telegrambotbase.handler.AbstractHandler;
import com.olebas.telegrambotbase.handler.DefaultHandler;
import com.olebas.telegrambotbase.handler.NotifyHandler;
import com.olebas.telegrambotbase.handler.SystemHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

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
        Message message = update.getMessage();
        Long chatId = update.getMessage().getChatId();

        ParsedCommand parsedCommand = new ParsedCommand(Command.NONE, "");

        if (message.hasText()) {
            parsedCommand = parser.getParsedCommand(message.getText());
        } else {
            Sticker sticker = message.getSticker();
            if (sticker != null) {
                parsedCommand = new ParsedCommand(Command.STICKER, sticker.getFileId());
            }
        }

        AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

        String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

        if (!"".equals(operationResult)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(operationResult);
            bot.sendQueue.add(sendMessage);
        }
    }

    private AbstractHandler getHandlerForCommand(Command command) {
        if (command == null) {
            log.warn("Null command accepted. This is not good scenario.");
            return new DefaultHandler(bot);
        }

        switch (command) {
            case START:
            case HELP:
            case STICKER:
                SystemHandler systemHandler = new SystemHandler(bot);
                log.info("Handler for command[" + command.toString() + "] is: " + systemHandler);
                return systemHandler;
            case ID:
                SystemHandler systemHandler = new SystemHandler(bot);
                log.info("Handler for command [" + command.toString() + "] is: " + systemHandler);
                return systemHandler;
            case NOTIFY:
                NotifyHandler notifyHandler = new NotifyHandler(bot);
                log.info("Handler for command [" + command.toString() + "] is: " + notifyHandler);
                return notifyHandler;
            default:
                log.info("Handler for command [" + command.toString() + "] not Set. Return DefaultHandler");
                return new DefaultHandler(bot);
        }
    }
}
