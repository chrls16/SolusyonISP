package com.example.solusyoninternetserviceprovider;

public class PendingRequestModel {
    private String applicationId;
    private String fullName;
    private String phone;
    private String plan;
    private String barangay;
    private String status;
    private String uid;

    public PendingRequestModel() {}

    public PendingRequestModel(String applicationId, String fullName, String phone, String plan, String barangay, String status, String uid) {
        this.applicationId = applicationId;
        this.fullName = fullName;
        this.phone = phone;
        this.plan = plan;
        this.barangay = barangay;
        this.status = status;
        this.uid = uid;
    }

    public String getApplicationId() { return applicationId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getPlan() { return plan; }
    public String getBarangay() { return barangay; }
    public String getStatus() { return status; }
    public String getUid() { return uid; }

    // Add Setters if needed for Firebase
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPlan(String plan) { this.plan = plan; }
    public void setBarangay(String barangay) { this.barangay = barangay; }
    public void setStatus(String status) { this.status = status; }
    public void setUid(String uid) { this.uid = uid; }
}