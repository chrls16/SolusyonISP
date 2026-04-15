package com.example.solusyoninternetserviceprovider;

public class PendingRequestModel {
    private String name, plan, refNo, address, contact;

    public PendingRequestModel(String name, String plan, String refNo, String address, String contact) {
        this.name = name;
        this.plan = plan;
        this.refNo = refNo;
        this.address = address;
        this.contact = contact;
    }

    public String getName() { return name; }
    public String getPlan() { return plan; }
    public String getRefNo() { return refNo; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
}