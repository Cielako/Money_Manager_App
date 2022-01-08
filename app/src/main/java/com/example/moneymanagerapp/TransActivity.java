package com.example.moneymanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

        transTitleEdit = findViewById(R.id.trans_title_text);
        transAmountEdit = findViewById(R.id.trans_amount_text);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TransactionLab.get(getApplicationContext()).updateTransaction(transUUID,transTitleEdit.getText().toString(),transAmountEdit.getText().toString(),transTypeSpinner.getSelectedItem().toString());
    }
}