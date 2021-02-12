package com.imfnly.nag;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Timer;
import java.util.TimerTask;

public class NagTimer {

    private static final long MINUTE_LENGTH = 60 * 1000;

    private Timer timer;
    private LocalTime nagTime;
    private LocalTime sleepTime;
    private LocalTime wakeTime;

    public NagTimer(LocalTime nagTime, LocalTime sleepTime, LocalTime wakeTime) {
        timer = new Timer();
        this.nagTime = nagTime;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;

        if (betweenTwoTimes(sleepTime, wakeTime, nagTime)) {
            throw new RuntimeException("Invalid time setup. NagTime is between sleep and wake time.");
        }
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
        LocalTime now = LocalTime.now();
        if (betweenTwoTimes(nagTime, sleepTime, now)) {

            long minutesTillSleep = timeTillSleep(ChronoUnit.MINUTES);
            String alertText = minutesTillSleep + " minutes till Sleep";

            Main.TRAY_ICON.displayMessage(PropertiesWrapper.getNotificationString(), alertText, MessageType.INFO);
        } else if (betweenTwoTimes(sleepTime, wakeTime, now)) {
            String shutdownCmd = "shutdown -s";
            try {
                Runtime.getRuntime().exec(shutdownCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long timeTillSleep(TemporalUnit unit) {

        LocalTime now = LocalTime.now();
        long timeTillSleep;

        if (now.isBefore(sleepTime)) {
            timeTillSleep = now.until(sleepTime, unit);
        } else {
            timeTillSleep = now.until(LocalTime.MAX, unit) + LocalTime.MIN.until(sleepTime, unit);
        }
        return timeTillSleep;
    }

    /**
     * This logic determines if the check is between the start and end time. If the
     * end time was 12:01 am and the start time was 11:59 pm, 12:00 am should be
     * considered between those, even though there shouldnt be a range between the
     * start and end time.
     * 
     * @param start     - beginning of range
     * @param end       - end of range
     * @param checkTime - time to check if in range
     * @return if start time is before end time, check needs to be after start AND
     *         before end. if start time is after end time, check needs to be after
     *         start OR before end
     */
    private static boolean betweenTwoTimes(LocalTime start, LocalTime end, LocalTime checkTime) {
        if (start.isBefore(end))
            return checkTime.isAfter(start) && checkTime.isBefore(end);
        else if (start.isAfter(end))
            return (checkTime.isAfter(start) || checkTime.isBefore(end));
        return checkTime.equals(start);
    }
}
