package com.example.solusyoninternetserviceprovider;

public class PaymentDue {
    private String name;
    private String accountNo;
    private String amount;
    private String dueDate;

    // 1. ADD THIS EMPTY CONSTRUCTOR (Mandatory for Firebase)
    public PaymentDue() {
    }

    // 2. KEEP YOUR EXISTING CONSTRUCTOR
    public PaymentDue(String name, String accountNo, String amount, String dueDate) {
        this.name = name;
        this.accountNo = accountNo;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    // ... (Keep all your Getters here)
    public String getName() { return name; }
    public String getAccountNo() { return accountNo; }
    public String getAmount() { return amount; }
    public String getDueDate() { return dueDate; }

    public String getInitials() {
        if (name == null || name.isEmpty()) return "??";
        String[] parts = name.split(" ");
        if (parts.length > 1) return (parts[0].substring(0,1) + parts[1].substring(0,1)).toUpperCase();
        return name.substring(0,1).toUpperCase();
    }
}