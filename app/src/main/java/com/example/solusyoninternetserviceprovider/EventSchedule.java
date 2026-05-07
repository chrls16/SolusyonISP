package com.example.solusyoninternetserviceprovider;

public class EventSchedule {
    private String eventType;
    private String address;
    private String time;
    private String date;
    private String key; // Added this field
    private String userUid; // ADD THIS FIELD

    public EventSchedule() {}

    public EventSchedule(String eventType, String address, String time, String date, String userUid) {
        this.eventType = eventType;
        this.address = address;
        this.time = time;
        this.date = date;
        this.userUid = userUid;
    }


    public String getEventType() { return eventType; }
    public String getAddress() { return address; }
    public String getTime() { return time; }
    public String getDate() { return date; }
    public String getKey() { return key; } // Added getter
    public String getUserUid() { return userUid; }
    public void setUserUid(String userUid) { this.userUid = userUid; }

    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setAddress(String address) { this.address = address; }
    public void setTime(String time) { this.time = time; }
    public void setDate(String date) { this.date = date; }
    public void setKey(String key) { this.key = key; } // Added setter
}