package com.example.moneymanagerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionLab {
    private  static TransactionLab sTransactionLab;
    private Double currentBalance;

    private SQLiteDatabase mDatabase;

    private TransactionLab(Context context){
        mDatabase = new DBHandler(context).getWritableDatabase();
    }

    public static  TransactionLab get(Context context){
        if(sTransactionLab == null){
            sTransactionLab = new TransactionLab(context);
        }
        return sTransactionLab;
    }

    public Transaction getTransaction(UUID id) {
        Cursor cursor = mDatabase.query("transactions", null, "_uuid = ?", new String[]{id.toString()},null,null,null);

        try {
            cursor.moveToFirst();
            String trans_title = cursor.getString(cursor.getColumnIndexOrThrow("trans_title"));
            String trans_type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String trans_amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
            Transaction transaction = new Transaction(id);
            transaction.setTitle(trans_title);
            transaction.setTranstype(trans_type);
            transaction.setAmount(Double.parseDouble(trans_amount));
            return transaction;
        }  finally {
            cursor.close();
        }

    }

    public List<Transaction> getTransactions() {

        Cursor cursor = mDatabase.query("transactions", null, null, null,null,null,"_id"+" DESC");
        List<Transaction> transactions = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String trans_title = cursor.getString(cursor.getColumnIndexOrThrow("trans_title"));
                String trans_type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String trans_amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                String uuid = cursor.getString(cursor.getColumnIndexOrThrow("_uuid"));
                Transaction transaction = new Transaction(UUID.fromString(uuid));
                transaction.setTitle(trans_title);
                transaction.setTranstype(trans_type);
                transaction.setAmount(Double.parseDouble(trans_amount));
                transactions.add(transaction);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return transactions;
    }

    public void updateTransaction(UUID id, String title, Double Amount, String Type) {
        Transaction transaction = new Transaction(id);
        transaction.setTitle(title);
        transaction.setAmount(Amount);
        transaction.setTranstype(Type);
        mDatabase.update("transactions",getContetntValues(transaction), "_uuid = ?",
                new String[] {id.toString()});
    }

    public void deleteTransaction(UUID id){
        mDatabase.delete("transactions","_uuid = ?", new String[]{id.toString()});
    }

    public void newTransaction(Transaction transaction){
        mDatabase.insert("transactions", null, getContetntValues(transaction));
    }

    public Double currBalance(){
        Cursor income = mDatabase.rawQuery("SELECT SUM(amount) AS total FROM transactions WHERE type = ?", new String[] {"income"});
        Cursor expense = mDatabase.rawQuery("SELECT SUM(amount) AS total FROM transactions WHERE type = ?", new String[] {"expense"});
        if(expense != null){
            expense.moveToFirst();
            System.out.println("expense: " + expense.getDouble(0));
        }
        if(income != null){
            income.moveToFirst();
            System.out.println("income: " +income.getDouble(0));
        }
        currentBalance = income.getDouble(0) - expense.getDouble(0);
        return currentBalance;

    }

    private ContentValues getContetntValues(Transaction transaction){
        ContentValues contentValues = new ContentValues();
        contentValues.put("_uuid", transaction.getId().toString());
        contentValues.put("trans_title", transaction.getTitle());
        contentValues.put("type", transaction.getTransType());
        contentValues.put("amount",transaction.getAmount());

        return contentValues;
    }
}
