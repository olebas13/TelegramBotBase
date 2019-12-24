package com.olebas.telegrambotbase;

import com.olebas.telegrambotbase.bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;

public class App {

    private static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        ApiContextInitializer.init();
        Bot bot = new Bot();
        bot.botConnect();
    }
}
