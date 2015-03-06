package com.ashoksm.thiraseela.vo;

/**
 * Created by Sheela on 6/3/2015.
 */
public class EventsVO {

    private String name;
    private String performance;
    private String location;
    private String date;
    private String time;
    private String url;

    public EventsVO(String name, String performance, String location, String date, String time, String url) {
        this.name = name;
        this.performance = performance;
        this.location = location;
        this.date = date;
        this.time = time;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
