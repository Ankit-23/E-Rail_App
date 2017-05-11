package com.example.user.e_rail.DataClass;

/**
 * Created by user on 4/26/2017.
 */
public class TrainBetween {
    private String tainInfo;
    private String source;
    private String destination;
    private String classCode;
    private String days;
    private String timeArr;
    private String timeDept;


    public String getTainInfo() {
        return tainInfo;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getDays() {
        return days;
    }

    public TrainBetween(String tainInfo, String source, String destination, String classCode, String days, String timeArr, String timeDept) {
        this.tainInfo = tainInfo;
        this.source = source;
        this.destination = destination;
        this.classCode = classCode;
        this.days = days;
        this.timeArr = timeArr;
        this.timeDept = timeDept;
    }


    public String getTimeArr() {
        return timeArr;
    }

    public String getTimeDept() {
        return timeDept;
    }
}

