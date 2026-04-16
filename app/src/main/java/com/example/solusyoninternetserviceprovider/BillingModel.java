package com.example.solusyoninternetserviceprovider;

public class BillingModel {
    private String invoiceNo;
    private String date;
    private String amount;
    private String status;

    public BillingModel(String invoiceNo, String date, String amount, String status) {
        this.invoiceNo = invoiceNo;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public String getInvoiceNo() { return invoiceNo; }
    public String getDate() { return date; }
    public String getAmount() { return amount; }
    public String getStatus() { return status; }
}
