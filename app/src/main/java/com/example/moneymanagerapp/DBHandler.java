package com.example.moneymanagerapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;


public class DBHandler extends SQLiteOpenHelper {
    public DBHandler( Context context) {
        super(context, "transactionDatabase.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transactions(" +
                "_id integer primary key autoincrement," +
                "_uuid," +
                "trans_title," +
                "type," +
                "amount)");

        /*for (int i =0; i < 20; i++){
            ContentValues values = new ContentValues();
            values.put("_uuid",UUID.randomUUID().toString());
            values.put("trans_title","test");
            if(i%2 ==0 ){
                values.put("type","income");
            }
            else{
                values.put("type","expense");
            }
            values.put("amount", 128.0);
            db.insert("transactions",null,values);
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
