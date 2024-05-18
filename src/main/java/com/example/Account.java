package com.example;

import java.util.ArrayList;
import java.util.List;
import com.example.Transaction; 

public class Account {
    private String accountId;
    private String name;
    private double balance;
    private List<Transaction> transactions;

    public Account(String accountId, String name, double balance) {
        this.accountId = accountId;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        balance = Math.round(balance * 100.0) / 100.0;
        //addTransaction(new Transaction(amount, accountId, null, "Deposit", 0));
    }

    public void withdraw(double amount) throws Exception {
        if (balance < amount) {
            throw new Exception("Not enough funds");
        }
        balance -= amount;
        balance = Math.round(balance * 100.0) / 100.0; 
       // addTransaction(new Transaction(amount, null, accountId, "Withdrawal", 0));
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void printTransactions() {
        transactions.forEach(System.out::println);
    }
}
