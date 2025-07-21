package com.momentum.dosein.models;

import java.io.Serializable;

public class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String time;
    private String medicineName;
    private String additional;  // notes
    private String startDate;
    private String endDate;

    public Reminder() { }

    public Reminder(String time,
                    String medicineName,
                    String additional,
                    String startDate,
                    String endDate) {
        this.time         = time;
        this.medicineName = medicineName;
        this.additional   = additional;
        this.startDate    = startDate;
        this.endDate      = endDate;
    }

    public String getTime()           { return time; }
    public void setTime(String time)  { this.time = time; }

    public String getMedicineName()              { return medicineName; }
    public void setMedicineName(String name)     { this.medicineName = name; }

    public String getAdditional()                { return additional; }
    public void setAdditional(String additional) { this.additional = additional; }

    public String getStartDate()               { return startDate; }
    public void setStartDate(String d)         { this.startDate = d; }

    public String getEndDate()                 { return endDate; }
    public void setEndDate(String d)           { this.endDate = d; }

    @Override
    public String toString() {
        String notePart = (additional == null || additional.isBlank())
                ? ""
                : " — " + additional;
        return String.format("%s — %s%s", time, medicineName, notePart);
    }
}
