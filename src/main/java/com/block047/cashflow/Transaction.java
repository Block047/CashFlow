package com.block047.cashflow;

//The transaction class :)

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(this.date, that.date) &&
                Objects.equals(this.amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount);
    }


}
