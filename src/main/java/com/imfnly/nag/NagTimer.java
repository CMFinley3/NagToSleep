package com.imfnly.nag;

import java.awt.TrayIcon.MessageType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class NagTimer {

    private static final long MINUTE_LENGTH = 60 * 1000;
    private static final String PROPERTIES_FILE = "config.properties";
    private static final String NAG_TIME_NAME = "nagTime";
    private static final String SLEEP_TIME_NAME = "sleepTime";
    private static final String WAKE_TIME_NAME = "wakeTime";
    private static final LocalTime DEFAULT_NAG_TIME = LocalTime.of(23, 0);
    private static final LocalTime DEFAULT_SLEEP_TIME = LocalTime.of(23, 30);
    private static final LocalTime DEFAULT_WAKE_TIME = LocalTime.of(0, 0);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    private Timer timer;
    private LocalTime nagTime;
    private LocalTime sleepTime;
    private LocalTime wakeTime;

    public NagTimer() throws IOException {
        timer = new Timer();

        Properties properties = new Properties();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(PROPERTIES_FILE);
        } catch (FileNotFoundException e) {
            FileOutputStream outputStream = new FileOutputStream(PROPERTIES_FILE);
            properties.put(NAG_TIME_NAME, DEFAULT_NAG_TIME.toString());
            properties.put(SLEEP_TIME_NAME, DEFAULT_SLEEP_TIME.toString());
            properties.put(WAKE_TIME_NAME, DEFAULT_WAKE_TIME.toString());
            properties.store(outputStream, "Saving Defaults");
            inputStream = new FileInputStream(PROPERTIES_FILE);
        }

        properties.load(inputStream);

        nagTime = LocalTime.parse(properties.getProperty(NAG_TIME_NAME), DATE_TIME_FORMATTER);
        sleepTime = LocalTime.parse(properties.getProperty(SLEEP_TIME_NAME), DATE_TIME_FORMATTER);
        wakeTime = LocalTime.parse(properties.getProperty(WAKE_TIME_NAME), DATE_TIME_FORMATTER);

        System.out.println("Times: " + nagTime + " " + sleepTime + " " + wakeTime);

    }

    public void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                nagLogic();
            }
        };

        timer.scheduleAtFixedRate(task, 0, MINUTE_LENGTH);
    }

    public void stop() {
        timer.cancel();
    }

    public void nagLogic() {
        if (LocalTime.now().isAfter(nagTime)) {
            Main.TRAY_ICON.displayMessage("HEY DIPSHIT", "GO TO SLEEP", MessageType.WARNING);
        }
    }
}
