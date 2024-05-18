package com.example;

public class BankSystem {
    public static void main(String[] args) {
        try {
            Bank bank = new Bank("MyBank", 10, 5);
            System.out.println("Welcome to " + bank.getBankName());

            Account account1 = bank.createAccount("User1", 1000);
            Account account2 = bank.createAccount("User2", 500);

            Transaction transaction1 = new Transaction(100, account1.getAccountId(), account2.getAccountId(), "Transfer to User2", true);
            Transaction transaction2 = new Transaction(50, account2.getAccountId(), account1.getAccountId(), "Transfer to User1", false);

            bank.performTransaction(transaction1);
            bank.performTransaction(transaction2);

            bank.deposit(account1.getAccountId(), 200);
            bank.withdraw(account2.getAccountId(), 100);

            bank.listTransactions(account1.getAccountId());
            bank.checkAccountBalance(account1.getAccountId());
            bank.listAccounts();

            System.out.println("Total Transaction Fee Amount: " + bank.getTotalTransactionFeeAmount());
            System.out.println("Total Transfer Amount: " + bank.getTotalTransferAmount());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
