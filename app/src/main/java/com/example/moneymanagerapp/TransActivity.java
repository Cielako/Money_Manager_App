package com.example.moneymanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.UUID;

public class TransActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private UUID transUUID;
    private Transaction transaction;
    private EditText transTitleEdit;
    private EditText transAmountEdit;
    private Spinner transTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        setTitle("Edit Transaction");
        transTitleEdit = findViewById(R.id.trans_title_text);
        transAmountEdit = findViewById(R.id.trans_amount_text);

        //float filter
        transAmountEdit.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = transAmountEdit.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        transTypeSpinner = findViewById(R.id.trans_type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.trans_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transTypeSpinner.setAdapter(adapter);
        transTypeSpinner.setOnItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            transUUID = UUID.fromString(extras.get("transactionUUID").toString());

            transaction = TransactionLab.get(this).getTransaction(transUUID);
            transTitleEdit.setText(transaction.getTitle());
            transAmountEdit.setText(transaction.getAmount().toString());
            transTypeSpinner.setSelection(getIndex(transTypeSpinner,transaction.getTransType()));
        }else{
            Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
        }


    }

    private int getIndex(Spinner transTypeSpinner, String transType) {

        int index = 0;

        for (int i=0;i<transTypeSpinner.getCount();i++){
            if (transTypeSpinner.getItemAtPosition(i).equals(transType)){
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_transaction_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_transaction:
                TransactionLab.get(getApplicationContext()).deleteTransaction(transUUID);
                Toast.makeText(this, "Transaction Deleted Successfully :)", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(transTitleEdit.getText().toString().isEmpty() || transAmountEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "Inputs Cant be empty :/", Toast.LENGTH_SHORT).show();
        }else{
            TransactionLab.get(getApplicationContext()).updateTransaction(transUUID,transTitleEdit.getText().toString(),Double.parseDouble(transAmountEdit.getText().toString()),transTypeSpinner.getSelectedItem().toString());
            Toast.makeText(this, "Transaction Edited Successfully :)", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}