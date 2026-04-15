package com.example.solusyoninternetserviceprovider;

public class EventSchedule {
    private String eventType;
    private String address;
    private String time;
    private String date; // Added to handle MM/DD/YYYY

    public EventSchedule() {}

    public EventSchedule(String eventType, String address, String time, String date) {
        this.eventType = eventType;
        this.address = address;
        this.time = time;
        this.date = date;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
