package com.imfnly.nag;

import java.time.LocalTime;

public enum ETimeKeys {

    NAG("nagTime", "Nag Time"),
    SLEEP("sleepTime", "Sleep Time"),
    WAKE("wakeTime", "Wake Time");

    private String key;
    private String displayName;

    ETimeKeys(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public LocalTime getTime() {
        return PropertiesWrapper.getTime(this);
    }
}
