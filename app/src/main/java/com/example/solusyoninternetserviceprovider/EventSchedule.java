package com.example.solusyoninternetserviceprovider;

public class EventSchedule {
    private String eventType;
    private String address;
    private String time;
    private String date;

    // 1. Required empty constructor for Firebase
    public EventSchedule() {}

    // 2. Full constructor
    public EventSchedule(String eventType, String address, String time, String date) {
        this.eventType = eventType;
        this.address = address;
        this.time = time;
        this.date = date;
    }

    // 3. Exact Getters the Adapters are looking for
    public String getEventType() { return eventType; }
    public String getAddress() { return address; }
    public String getTime() { return time; }
    public String getDate() { return date; }

    // 4. Setters
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setAddress(String address) { this.address = address; }
    public void setTime(String time) { this.time = time; }
    public void setDate(String date) { this.date = date; }
}