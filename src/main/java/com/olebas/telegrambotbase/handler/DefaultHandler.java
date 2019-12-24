package com.olebas.telegrambotbase.handler;

import com.olebas.telegrambotbase.bot.Bot;
import com.olebas.telegrambotbase.command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultHandler extends AbstractHandler {

    private static final Logger log = Logger.getLogger(DefaultHandler.class);

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        return "";
    }
}
