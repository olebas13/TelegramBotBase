package com.olebas.telegrambotbase.handler;

import com.olebas.telegrambotbase.bot.Bot;
import com.olebas.telegrambotbase.command.Command;
import com.olebas.telegrambotbase.command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    private Object getMessageHelp(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("*This is help message*").append(END_LINE).append(END_LINE);
        text.append("[/start](/start) - show start message").append(END_LINE);
        text.append("[/help](/help) - show help message").append(END_LINE);
        text.append("[/id](/id) - know your ID in telegram ").append(END_LINE);
        text.append("/*notify* _time-in-sec_  - receive notification from me after the specified time").append(END_LINE);
        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private Object getMessageStart(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("Hello! I'm *").append(bot.getBotUsername()).append("*").append(END_LINE);
        text.append("I created for Olebas education.").append(END_LINE);
        text.append("All that I can do - you can see calling the command [/help](/help)");
        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
