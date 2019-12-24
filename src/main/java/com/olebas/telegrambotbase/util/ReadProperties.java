package com.olebas.telegrambotbase.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {

    private Properties prop = new Properties();

    public ReadProperties() {
        File file = new File((System.getProperty("user.dir") + "/src/main/resources/config.properties"));

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            prop.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return prop.getProperty("BOT_USERNAME");
    }

    public String getBotToken() {
        return prop.getProperty("BOT_TOKEN");
    }
}
