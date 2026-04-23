package com.example.solusyoninternetserviceprovider;

public class UserActivityItem {
    private String title;
    private String invoiceId;
    private String amount;
    private String status;

    public UserActivityItem(String title, String invoiceId, String amount, String status) {
        this.title = title;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.status = status;
    }

    // Getters
    public String getTitle() { return title; }
    public String getInvoiceId() { return invoiceId; }
    public String getAmount() { return amount; }
    public String getStatus() { return status; }
}