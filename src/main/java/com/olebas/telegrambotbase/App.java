package com.olebas.telegrambotbase;

import com.olebas.telegrambotbase.bot.Bot;
import com.olebas.telegrambotbase.service.MessageReceiver;
import com.olebas.telegrambotbase.service.MessageSender;
import com.olebas.telegrambotbase.util.ReadProperties;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class App {

    private static final Logger log = Logger.getLogger(App.class);
    private static final ReadProperties prop = new ReadProperties();
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        Bot bot = new Bot();

        MessageReceiver messageReciever = new MessageReceiver(bot);
        MessageSender messageSender = new MessageSender(bot);

        bot.botConnect();

        Thread receiver = new Thread(messageReciever);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

        sendStartReport(bot);
    }

    private static void sendStartReport(Bot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(prop.getBotAdmin());
        sendMessage.setText("STARTED!");
        bot.sendQueue.add(sendMessage);
    }
}
