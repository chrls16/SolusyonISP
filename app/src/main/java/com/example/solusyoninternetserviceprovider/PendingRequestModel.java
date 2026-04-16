package com.example.solusyoninternetserviceprovider;

public class PendingRequestModel {
    // Variables updated to match your Firebase keys exactly
    private String name;
    private String planType;   // Matches 'planType' in JS
    private String referenceNo; // Matches 'referenceNo' in JS
    private String address;
    private String contact;    // Matches 'phone' in JS (ensure names align)

    // 1. CRITICAL: Empty constructor for Firebase
    public PendingRequestModel() {
    }

    // 2. Main constructor for manual use
    public PendingRequestModel(String name, String planType, String referenceNo, String address, String contact) {
        this.name = name;
        this.planType = planType;
        this.referenceNo = referenceNo;
        this.address = address;
        this.contact = contact;
    }

    // 3. Getters (Firebase needs these to read data)
    public String getName() { return name; }
    public String getPlanType() { return planType; }
    public String getReferenceNo() { return referenceNo; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }

    // 4. Setters (Firebase needs these to write data into the object)
    public void setName(String name) { this.name = name; }
    public void setPlanType(String planType) { this.planType = planType; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }
    public void setAddress(String address) { this.address = address; }
    public void setContact(String contact) { this.contact = contact; }
}