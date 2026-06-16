package com.block047.cashflow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

//Saves and allows access to financial data

public class FinancialData {

    static String filePath = "data.json";
    static String jarPath = System.getProperty("user.dir");
    static List<Transaction> transactions = new ArrayList<>();
    static Map<String, Object> data = new HashMap<>();
    static Double budget;
    static Double savings;

    public static Double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static Double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static void setBudget(Double budget) throws Exception {
        FinancialData.budget = budget;
        data.put("budget", budget);
        save();
    }

    public static void setSavings(Double savings) throws Exception {
        FinancialData.savings = savings;
        data.put("savings", savings);
        save();
    }

    public static Double getBudget() {
        return budget;
    }

    public static Double getSavings() {
        return savings;
    }

    public static void addTransaction(Transaction transaction) throws Exception {
        transactions.add(transaction);
        data.put("transactions", transactions);
        System.out.println(transaction.toString());
        System.out.println(transactions.toString());
        save();
    }

    public static void removeTransaction(Transaction transaction) throws Exception {
        transactions.remove(transaction);
        save();
    }

    public static void removeTransactionByDate(Long date) throws Exception {
        transactions.removeIf(t -> Objects.equals(t.getDate(), date));
        data.put("transactions", transactions);
        save();
    }


    public static Transaction getTransaction(Long date){
        for (Transaction t : transactions){
            if (Objects.equals(t.getDate(), date)){
                return t;
            }
        }
        return null;
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
        gson.toJson(data, writer);
        writer.flush();
        writer.close();
    }

    //Loads the data
    public static void load() throws Exception {
        Gson gson = new Gson();
        File file = new File(jarPath + "/" + filePath);
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
            data = gson.fromJson(reader, type);
            String raw = gson.toJson(data.get("transactions"));
            Type list = new TypeToken<List<Transaction>>(){}.getType();
            transactions = gson.fromJson(raw, list) != null ? gson.fromJson(raw,list) : new ArrayList<>();
            savings = data.get("savings") != null ? ((Number) data.get("savings")).doubleValue() : 0.0;
            budget = data.get("budget") != null ? ((Number) data.get("budget")).doubleValue() : 0.0;
            reader.close();
        }
    }

}
