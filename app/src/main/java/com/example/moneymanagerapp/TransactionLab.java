package com.example.moneymanagerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionLab {
    private  static TransactionLab sTransationLab;

    private SQLiteDatabase mDatabase;

    private TransactionLab(Context context){
        mDatabase = new DBHandler(context).getWritableDatabase();
    }

    public static  TransactionLab get(Context context){
        if(sTransationLab == null){
            sTransationLab = new TransactionLab(context);
        }
        return sTransationLab;
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

        Cursor cursor = mDatabase.query("transactions", null, null, null,null,null,null);
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
}
