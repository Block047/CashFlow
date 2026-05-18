package com.studytoolserver.cashflow;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

//Saves and allows access to financial data

public class FinancialData {

    static String filePath = "data.json";
    static String jarPath = System.getProperty("java.class.path");
    static List<Transaction> transactions = new ArrayList<>();

    public static void addTransaction(Transaction transaction) throws Exception {
        transactions.add(transaction);
        System.out.println(transaction.toString());
        System.out.println(transactions.toString());
        save();
    }

    public static List<Transaction> getTransactions() {
        return transactions;
    }

    public static Double getTotalCashFlow() {
        //Sums up all the transactions to get the total cash flow
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    //Saves the data
    public static void save() throws Exception {
        //Gson is a library that allows the conversion of Java objects to Json and vice versa.
        Gson gson = new Gson();
        File file = new File(jarPath + "/" + filePath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        Writer writer = new FileWriter(file);
        gson.toJson(transactions, writer);
        writer.flush();
        writer.close();
    }

    //Loads the data
    public static void load() throws Exception {
        Gson gson = new Gson();
        File file = new File(jarPath + "/" + filePath);
        if (file.exists()) {
            Transaction[] loadedTransactions = gson.fromJson(new java.io.FileReader(file), Transaction[].class);
            if (loadedTransactions != null) {
                transactions = new ArrayList<>(List.of(loadedTransactions));
            }
        }
    }

}
