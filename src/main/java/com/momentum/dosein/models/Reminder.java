package com.momentum.dosein.models;

public class Reminder {
    private final String time;
    private final String description;

    public Reminder(String time, String description) {
        this.time = time;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}
