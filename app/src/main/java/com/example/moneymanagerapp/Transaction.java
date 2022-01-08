package com.example.moneymanagerapp;

import java.util.UUID;

public class Transaction {
    private UUID mId;
    private String mTitle;
    private String mTransType;
    private double mAmount;

    public Transaction(){
        mId = UUID.randomUUID();
    }
    //Setters for transaction
    public Transaction(UUID id){this.mId = id;}

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setTranstype(String mTranstype) { this.mTransType = mTranstype; }

    public void  setAmount (double mAmount ){this.mAmount = mAmount;}

    //Getters for transaction
    public UUID getId() {return mId;}

    public String getTitle() {return mTitle;}

    public String getTransType() {return mTransType;}

    public Double getAmount() {return mAmount;}

}

