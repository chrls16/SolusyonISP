package com.example.solusyoninternetserviceprovider;


public class SubscriberModel {
    private String name, subscriberId, accountStatus, locationCity;
    private String planTag, packageTier, speed, monthlyBilling;
    private String email, phone, fullAddress;
    private boolean isActive; // Determines the green dot and status badge

    public SubscriberModel(String name, String subscriberId, String accountStatus, String locationCity,
                           String planTag, String packageTier, String speed, String monthlyBilling,
                           String email, String phone, String fullAddress, boolean isActive) {
        this.name = name;
        this.subscriberId = subscriberId;
        this.accountStatus = accountStatus;
        this.locationCity = locationCity;
        this.planTag = planTag;
        this.packageTier = packageTier;
        this.speed = speed;
        this.monthlyBilling = monthlyBilling;
        this.email = email;
        this.phone = phone;
        this.fullAddress = fullAddress;
        this.isActive = isActive;
    }

    // Getters
    public String getName() { return name; }
    public String getSubscriberId() { return subscriberId; }
    public String getAccountStatus() { return accountStatus; }
    public String getLocationCity() { return locationCity; }
    public String getPlanTag() { return planTag; }
    public String getPackageTier() { return packageTier; }
    public String getSpeed() { return speed; }
    public String getMonthlyBilling() { return monthlyBilling; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getFullAddress() { return fullAddress; }
    public boolean isActive() { return isActive; }
}