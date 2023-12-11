package com.example.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private String eventId;
    private String eventName;
    private String date;
    private String time;
    private String location;
    private String organization;
    private String description;

    public Event() {
        // default constructor
    }

    public Event(String eventId, String eventName, String date, String time, String location, String organization, String description) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.date = date; // date format MMDDYYY
        this.time = time;
        this.location = location;
        this.organization = organization;
        this.description = description;
    }

    public Event(String eventName, String date, String time, String location, String organization, String description) {
        this.eventName = eventName;
        this.date = date; // date format MMDDYYY
        this.time = time;
        this.location = location;
        this.organization = organization;
        this.description = description;
    }

    //in order too send it from options to selection page
    protected Event(Parcel in) {
        eventId = in.readString();
        eventName = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        organization = in.readString();
        description = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventName);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(organization);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    //getters and setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}




