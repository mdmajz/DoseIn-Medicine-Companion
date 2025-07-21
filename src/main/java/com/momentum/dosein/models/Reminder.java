package com.momentum.dosein.models;

import java.io.Serializable;
import java.time.LocalTime;

public class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalTime time;
    private String medicine;
    private String notes;

    public Reminder(LocalTime time, String medicine, String notes) {
        this.time = time;
        this.medicine = medicine;
        this.notes = notes;
    }

    public LocalTime getTime() { return time; }
    public String getMedicine() { return medicine; }
    public String getNotes() { return notes; }
}
