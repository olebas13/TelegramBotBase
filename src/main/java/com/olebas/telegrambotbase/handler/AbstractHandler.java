package com.olebas.telegrambotbase.handler;

import com.olebas.telegrambotbase.bot.Bot;
import com.olebas.telegrambotbase.command.ParsedCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {

    Bot bot;

    AbstractHandler(Bot bot) {
        this.bot = bot;
    }

    public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update);
}
