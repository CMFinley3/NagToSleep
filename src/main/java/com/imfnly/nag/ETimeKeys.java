package com.imfnly.nag;

public enum ETimeKeys {

    NAG("nagTime"), SLEEP("sleepTime"), WAKE("wakeTime");

    private String key;

    ETimeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
