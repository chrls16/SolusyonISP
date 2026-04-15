// PaymentDue.java
package com.example.solusyoninternetserviceprovider;

public class PaymentDue {
    private String avatarInitials;
    private String name;
    private String accountDetails;
    private String amount;
    private String overdueStatus;

    public PaymentDue() {}

    public PaymentDue(String avatarInitials, String name, String accountDetails, String amount, String overdueStatus) {
        this.avatarInitials = avatarInitials;
        this.name = name;
        this.accountDetails = accountDetails;
        this.amount = amount;
        this.overdueStatus = overdueStatus;
    }

    public String getAvatarInitials() { return avatarInitials; }
    public void setAvatarInitials(String avatarInitials) { this.avatarInitials = avatarInitials; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAccountDetails() { return accountDetails; }
    public void setAccountDetails(String accountDetails) { this.accountDetails = accountDetails; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getOverdueStatus() { return overdueStatus; }
    public void setOverdueStatus(String overdueStatus) { this.overdueStatus = overdueStatus; }
}