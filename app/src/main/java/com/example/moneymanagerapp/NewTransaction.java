package com.example.moneymanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewTransaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText transTitleEdit;
    private EditText transAmountEdit;
    private Spinner transTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        transTitleEdit = findViewById(R.id.new_trans_title_text);
        transAmountEdit = findViewById(R.id.new_trans_amount_text);
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
        transTypeSpinner = findViewById(R.id.new_trans_type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.trans_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transTypeSpinner.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(transTitleEdit.getText().toString().isEmpty() || transAmountEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "Inputs Cant be empty :/", Toast.LENGTH_SHORT).show();
        }else{
            Transaction transaction = new Transaction();
            transaction.setTitle(transTitleEdit.getText().toString());
            transaction.setAmount(Double.parseDouble(transAmountEdit.getText().toString()));
            transaction.setTranstype(transTypeSpinner.getSelectedItem().toString());

            TransactionLab.get(getApplicationContext()).newTransaction(transaction);
            Toast.makeText(this, "Transaction Added Successfully :)", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}