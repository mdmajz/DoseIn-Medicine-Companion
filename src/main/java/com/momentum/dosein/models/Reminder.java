package com.momentum.dosein.models;

public class Reminder {
    private final String time;
    private final String pillName;
    private final String additionalInfo;
    private final String startDate;
    private final String endDate;

    public Reminder(String time, String pillName, String additionalInfo, String startDate, String endDate) {
        this.time = time;
        this.pillName = pillName;
        this.additionalInfo = additionalInfo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTime()           { return time; }
    public String getPillName()       { return pillName; }
    public String getAdditionalInfo(){ return additionalInfo; }
    public String getStartDate()      { return startDate; }
    public String getEndDate()        { return endDate; }

    /** for grouping and display in the lists */
    public String getDescription() {
        return pillName + (additionalInfo.isEmpty() ? "" : " – " + additionalInfo);
    }
}
