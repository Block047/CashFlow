package com.studytoolserver.cashflow;

//The transaction class :)

public class Transaction {

    Double amount;
    String description;
    Long date;

    public Transaction(Double amount, String description, Long date) throws Exception {
        this.amount = amount;
        this.description = description;
        this.date = date;
        FinancialData.addTransaction(this);
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Long getDate() {
        return date;
    }

}
