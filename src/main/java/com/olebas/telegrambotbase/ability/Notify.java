package com.olebas.telegrambotbase.ability;

import com.olebas.telegrambotbase.bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

public class Notify implements Runnable {

    private static final Logger log = Logger.getLogger(Notify.class);
    private static final int MILLISEC_IN_SEC = 1000;
    private Bot bot;
    private long delayInMillisec;
    private String chatId;

    public Notify(Bot bot, String chatId, long delayInMillisec) {
        this.bot = bot;
        this.chatId = chatId;
        this.delayInMillisec = delayInMillisec;
        log.debug("CREATE. " + toString());
    }

    @Override
    public void run() {
        log.info("RUN. " + toString());
        bot.sendQueue.add(getFirstMessage());
        try {
            Thread.sleep(delayInMillisec);
            bot.sendQueue.add(getSecondSticker());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("FINISH. " + toString());
    }

    private SendSticker getSecondSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(chatId);
        sendSticker.setSticker("CAADBQADiQMAAukKyAPZH7wCI2BwFxYE");
        return sendSticker;
    }

    private SendMessage getFirstMessage() {
        return new SendMessage(chatId, "I will send you notify after " + delayInMillisec / MILLISEC_IN_SEC + " sec");
    }

    private SendMessage getSecondMessage() {
        return new SendMessage(chatId, "This is notify message. Thanks for using :)");
    }

    @Override
    public String toString() {
        return "Notify{" +
                "bot=" + bot +
                ", delayInMillisec=" + delayInMillisec +
                ", chatId='" + chatId + '\'' +
                '}';
    }
}
