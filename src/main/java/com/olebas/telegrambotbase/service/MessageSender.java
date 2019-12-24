package com.olebas.telegrambotbase.service;

import com.olebas.telegrambotbase.bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSender implements Runnable {

    private static final Logger log = Logger.getLogger(MessageSender.class);
    private final int SENDER_SLEEP_TIME = 1000;

    private Bot bot;

    public MessageSender(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        log.info("[STARTED] MsgSender. Bot class: " + bot);

        try {
            while (true) {
                for (Object object = bot.sendQueue.poll(); object != null; object = bot.sendQueue.poll()) {
                    log.debug("Get new msg to send " + object);
                    send(object);
                }
                try {
                    Thread.sleep(SENDER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    log.error("Take interrupt while operate msg list", e);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void send(Object object) {
        try {
            MessageType messageType = messageType(object);
            switch (messageType) {
                case EXECUTE:
                    BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                    log.debug("Use Execute for " + object);
                    bot.execute(message);
                    break;
                case STICKER:
                    SendSticker sendSticker = (SendSticker) object;
                    log.debug("Use SendSticker for " + object);
                    bot.execute(sendSticker);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private MessageType messageType(Object object) {
        if (object instanceof SendSticker) {
            return MessageType.STICKER;
        }

        if (object instanceof BotApiMethod) {
            return MessageType.EXECUTE;
        }

        return MessageType.NOT_DETECTED;
    }

    enum MessageType {
        EXECUTE, STICKER, NOT_DETECTED
    }
}
