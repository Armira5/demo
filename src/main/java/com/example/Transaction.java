package com.example;

public class Transaction {
    private double amount;
    private String fromAccountId;
    private String toAccountId;
    private String reason;
    private double fee;

    public Transaction(double amount, String fromAccountId, String toAccountId, String reason, boolean isFlatFee) {
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.reason = reason;
        this.fee = isFlatFee ? 5 : amount * 0.05;
    }

    public double getAmount() {
        return amount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public String getReason() {
        return reason;
    }

    public double getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return String.format("Transaction [amount=%.2f, fromAccountId=%s, toAccountId=%s, reason=%s, fee=%.2f]",
                amount, fromAccountId, toAccountId, reason, fee);
    }
}
