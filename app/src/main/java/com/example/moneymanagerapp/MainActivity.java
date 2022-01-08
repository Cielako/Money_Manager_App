package com.example.moneymanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Transaction> mTransactions;
    private RecyclerView recyclerView;
    private TransactionListAdapter transactionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        mTransactions = TransactionLab.get(getApplicationContext()).getTransactions();
        transactionListAdapter = new TransactionListAdapter(this, mTransactions);
        recyclerView.setAdapter(transactionListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onResume() {
        super.onResume();
        mTransactions = TransactionLab.get(getApplicationContext()).getTransactions();
        transactionListAdapter = new TransactionListAdapter(this, mTransactions);
        recyclerView.setAdapter(transactionListAdapter);
    }
}