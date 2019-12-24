package com.olebas.telegrambotbase.handler;

import com.olebas.telegrambotbase.bot.Bot;
import com.olebas.telegrambotbase.command.Command;
import com.olebas.telegrambotbase.command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SystemHandler extends AbstractHandler {

    private static final Logger log = Logger.getLogger(SystemHandler.class);
    private final String END_LINE = "\n";

    public SystemHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();

        switch (command) {
            case START:
                bot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(chatId));
                break;
            case ID:
                return "Your telegramID: " + update.getMessage().getFrom().getId();
        }
        return "";
    }

    private Object getMessageStart(String chatId) {
    }
}
