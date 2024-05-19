package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bank {
    private String bankName;
    private List<Account> accounts;
    private double totalTransactionFeeAmount;
    private double totalTransferAmount;
    private double transactionFlatFeeAmount;
    private double transactionPercentFeeValue;
    private List<Transaction> transactions;
    private Connection connection;

    public Bank(String bankName, double flatFee, double percentFee) {
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
        this.totalTransactionFeeAmount = 0;
        this.totalTransferAmount = 0;
        this.transactionFlatFeeAmount = flatFee;
        this.transactionPercentFeeValue = percentFee;
        this.transactions = new ArrayList<>();
        System.out.println("Bank " + bankName + " created with flat fee: " + flatFee + " and percent fee: " + percentFee);

        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE accounts (accountId VARCHAR(255), name VARCHAR(255), balance DOUBLE)");
            stmt.execute("CREATE TABLE transactions (amount DOUBLE, fromAccountId VARCHAR(255), toAccountId VARCHAR(255), reason VARCHAR(255), fee DOUBLE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account createAccount(String name, double initialBalance) {
        Account account = new Account(UUID.randomUUID().toString(), name, initialBalance);
        accounts.add(account);
        saveAccount(account); 
        return account;
    }

    public Account getAccount(String accountId) throws Exception {
        return accounts.stream()
                .filter(acc -> acc.getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new Exception("Account not found"));
    }

    public void performTransaction(Transaction transaction) throws Exception {
        transactions.add(transaction);
        saveTransaction(transaction);

        Account fromAccount = getAccount(transaction.getFromAccountId());
        Account toAccount = getAccount(transaction.getToAccountId());

        double amount = transaction.getAmount();
        double fee = transaction.getFee();
        

        if (fromAccount.getBalance() < (amount + fee)) {
            throw new Exception("Not enough funds");
        }

        fromAccount.withdraw(amount + fee);
        toAccount.deposit(amount);

        totalTransactionFeeAmount += fee;
        totalTransactionFeeAmount = Math.round(totalTransactionFeeAmount * 100.0) / 100.0;
        totalTransferAmount += amount;
        totalTransferAmount = Math.round(totalTransferAmount * 100.0) / 100.0;

        fromAccount.addTransaction(transaction);
        toAccount.addTransaction(transaction);
    }

    public void deposit(String accountId, double amount) throws Exception {
        Account account = getAccount(accountId);
        account.deposit(amount);
    }

    public void withdraw(String accountId, double amount) throws Exception {
        Account account = getAccount(accountId);
        account.withdraw(amount);
    }

    public void listTransactions(String accountId) throws Exception {
        Account account = getAccount(accountId);
        account.printTransactions();
    }

    public void checkAccountBalance(String accountId) throws Exception {
        Account account = getAccount(accountId);
        System.out.println("Balance: " + account.getBalance());
    }

    public void listAccounts() {
        accounts.forEach(account -> System.out.println("Account ID: " + account.getAccountId() + ", Name: " + account.getName() + ", Balance: " + account.getBalance()));
    }

    public double getTotalTransactionFeeAmount() {
        return totalTransactionFeeAmount;
    }

    public double getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public String getBankName() {
        return bankName;
    }
     private void saveAccount(Account account) {
        try (Statement stmt = connection.createStatement()) {
            String query = "INSERT INTO accounts (accountId, name, balance) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, account.getAccountId());
            pstmt.setString(2, account.getName());
            pstmt.setDouble(3, account.getBalance());
            pstmt.executeUpdate();
        } catch (SQLException e) {
           
            System.err.println("Error saving account: " + e.getMessage());
        }
    }

    private void saveTransaction(Transaction transaction) {
        try (Statement stmt = connection.createStatement()) {
            String query = "INSERT INTO transactions (amount, fromAccountId, toAccountId, reason, fee) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setDouble(1, transaction.getAmount());
            pstmt.setString(2, transaction.getFromAccountId());
            pstmt.setString(3, transaction.getToAccountId());
            pstmt.setString(4, transaction.getReason());
            pstmt.setDouble(5, transaction.getFee());
            pstmt.executeUpdate();
        } catch (SQLException e) {

            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }
}
