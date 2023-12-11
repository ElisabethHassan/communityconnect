package com.example.finalproject;

public class Event {
    private String eventId;
    private String eventName;
    private String date;
    private String time;
    private String location;
    private String organization;


    public Event() {
    }

    public Event(String eventId, String eventName, String date, String time, String location, String organization) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.date = date; //date format MMDDYYY
        this.time = time;
        this.location = location;
        this.organization = organization;
    }
    public Event(String eventName, String date, String time, String location, String organization) {
        this.eventName = eventName;
        this.date = date; //date format MMDDYYY
        this.time = time;
        this.location = location;
        this.organization = organization;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
