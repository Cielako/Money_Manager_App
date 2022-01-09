package com.example.moneymanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_of_transactions_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_transaction);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                transactionListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_transaction:
                Intent intent = new Intent(this, NewTransaction.class);
                startActivity(intent);
                return true;
            case R.id.search_transaction:
                int id = item.getItemId();
                if(id == R.id.search_transaction) {
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTransactions = TransactionLab.get(getApplicationContext()).getTransactions();
        transactionListAdapter = new TransactionListAdapter(this, mTransactions);
        recyclerView.setAdapter(transactionListAdapter);
    }
}