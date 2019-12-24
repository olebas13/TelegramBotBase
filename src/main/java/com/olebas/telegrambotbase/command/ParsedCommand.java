package com.olebas.telegrambotbase.command;

public class ParsedCommand {

    private Command command;
    private String text;

    public ParsedCommand() {
        command = Command.NONE;
        text = "";
    }

    public ParsedCommand(Command command, String text) {
        this.command = command;
        this.text = text;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
