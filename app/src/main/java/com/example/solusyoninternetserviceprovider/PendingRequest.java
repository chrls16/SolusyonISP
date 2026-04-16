package com.example.solusyoninternetserviceprovider;

public class PendingRequest {

    // This holds the unique Firebase ID so we know exactly which application to approve/reject
    private String firebaseKey;

    // These match the fields you save from your Netlify website
    private String name;
    private String address;
    private String planType;
    private String email;
    private String contact;
    private String status;

    // Required empty public constructor for Firebase
    public PendingRequest() {
    }

    // Getters and Setters
    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}