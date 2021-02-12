package com.imfnly.nag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class PropertiesWrapper {
    private static final String PROPERTIES_FILE = "config.properties";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
    private static final LocalTime DEFAULT_NAG_TIME = LocalTime.of(23, 0);
    private static final LocalTime DEFAULT_SLEEP_TIME = LocalTime.of(23, 30);
    private static final LocalTime DEFAULT_WAKE_TIME = LocalTime.of(0, 0);
    private static final String NOTIFICATION_KEY = "notification";
    private static final String DEFAULT_NOTIFICATION = "Computing Shutting Down Soon";
    private static PropertiesWrapper INSTANCE;

    private Properties properties;

    private PropertiesWrapper() throws IOException {
        properties = new Properties();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(PROPERTIES_FILE);
        } catch (FileNotFoundException e) {
            FileOutputStream outputStream = new FileOutputStream(PROPERTIES_FILE);
            properties.put(ETimeKeys.NAG.getKey(), DEFAULT_NAG_TIME.toString());
            properties.put(ETimeKeys.SLEEP.getKey(), DEFAULT_SLEEP_TIME.toString());
            properties.put(ETimeKeys.WAKE.getKey(), DEFAULT_WAKE_TIME.toString());
            properties.put(NOTIFICATION_KEY, DEFAULT_NOTIFICATION);
            properties.store(outputStream, "Saving Defaults");
            inputStream = new FileInputStream(PROPERTIES_FILE);
        }

        properties.load(inputStream);
    }

    private String getProperty(String keyString) {
        return properties.getProperty(keyString);
    }

    private boolean setProperty(String keyString, String valueString) {

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(PROPERTIES_FILE);
            properties.setProperty(keyString, valueString);
            properties.store(outputStream, "Saving Defaults");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static PropertiesWrapper getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new PropertiesWrapper();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read properties file");
            }
        }
        return INSTANCE;
    }

    public static LocalTime getTime(ETimeKeys key) {
        String keyString = key.getKey();
        String valueString = getInstance().getProperty(keyString);
        LocalTime time = LocalTime.parse(valueString, DATE_TIME_FORMATTER);
        return time;
    }

    public static boolean setTime(ETimeKeys key, String timeString) {
        try {
            LocalTime.parse(timeString, DATE_TIME_FORMATTER);
        } catch (DateTimeException exception) {
            return false;
        }

        return getInstance().setProperty(key.getKey(), timeString);
    }

    public static String getNotificationString() {
        return getInstance().getProperty(NOTIFICATION_KEY);
    }
}
