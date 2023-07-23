package com.example.mobileshop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRecords extends AppCompatActivity {

    private Spinner companySpinner;
    private Spinner modelSpinner;
    private EditText priceEditText;
    private EditText nameEditText;
    private EditText idCardEditText;

    private static final String[] ANDROID_MODELS = {
            "Samsung S23",
            "Samsung S22",
            "Redmi 10C",
            "Infinix Hot 12"
    };

    private static final String[] IPHONE_MODELS = {
            "iPhone 13",
            "iPhone 13 Pro",
            "iPhone 13 Pro Max",
            "iPhone 14"
    };

    private static final int[] ANDROID_PRICES = {
            232999,
            399999,
            35999,
            32999
    };

    private static final int[] IPHONE_PRICES = {
            226100,
            344999,
            369600,
            385999
    };

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_records);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records");

        companySpinner = findViewById(R.id.spinnerMobileCompany);
        modelSpinner = findViewById(R.id.spinnerMobileModel);
        priceEditText = findViewById(R.id.editTextPrice);
        nameEditText = findViewById(R.id.editTextName);
        idCardEditText = findViewById(R.id.editTextIdCard);
        Button saveButton = findViewById(R.id.btn_save_record);

        ArrayAdapter<CharSequence> companyAdapter = ArrayAdapter.createFromResource(
                this, R.array.mobile_companies, android.R.layout.simple_spinner_item);
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companySpinner.setAdapter(companyAdapter);

        companySpinner.setSelection(0, false);
        modelSpinner.setEnabled(false);
        priceEditText.setEnabled(false);

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    modelSpinner.setEnabled(false);
                    priceEditText.setEnabled(false);
                } else if (position == 1) {
                    ArrayAdapter<String> androidModelAdapter = new ArrayAdapter<>(AddRecords.this,
                            android.R.layout.simple_spinner_item, ANDROID_MODELS);
                    modelSpinner.setAdapter(androidModelAdapter);
                    modelSpinner.setEnabled(true);
                    priceEditText.setEnabled(true);
                    priceEditText.setText(String.valueOf(ANDROID_PRICES[0]));
                } else if (position == 2) {
                    ArrayAdapter<String> iPhoneModelAdapter = new ArrayAdapter<>(AddRecords.this,
                            android.R.layout.simple_spinner_item, IPHONE_MODELS);
                    modelSpinner.setAdapter(iPhoneModelAdapter);
                    modelSpinner.setEnabled(true);
                    priceEditText.setEnabled(true);
                    priceEditText.setText(String.valueOf(IPHONE_PRICES[0]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedCompanyPosition = companySpinner.getSelectedItemPosition();
                if (selectedCompanyPosition == 1) {
                    priceEditText.setText(String.valueOf(ANDROID_PRICES[position]));
                } else if (selectedCompanyPosition == 2) {
                    priceEditText.setText(String.valueOf(IPHONE_PRICES[position]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().toUpperCase();
                String idCard = idCardEditText.getText().toString();
                String selectedCompany = companySpinner.getSelectedItem().toString().toUpperCase();
                String selectedModel = modelSpinner.getSelectedItem().toString().toUpperCase();
                String price = priceEditText.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(AddRecords.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                } else if (idCard.isEmpty()) {
                    Toast.makeText(AddRecords.this, "Please enter your ID card", Toast.LENGTH_SHORT).show();
                } else if (!isValidIdCard(idCard)) {
                    Toast.makeText(AddRecords.this, "Please enter a valid ID card number", Toast.LENGTH_SHORT).show();
                } else if (selectedCompany.equals("Company")) {
                    Toast.makeText(AddRecords.this, "Please select a company", Toast.LENGTH_SHORT).show();
                } else if (selectedModel.equals("Model")) {
                    Toast.makeText(AddRecords.this, "Please select a model", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty()) {
                    Toast.makeText(AddRecords.this, "Please enter the price", Toast.LENGTH_SHORT).show();
                } else {


                    // Create a new record object
                    MyRecord record = new MyRecord(name, idCard, selectedCompany, selectedModel, price);

                    // Save the record to Firebase database
                    String recordId = databaseReference.push().getKey();
                    databaseReference.child(recordId).setValue(record)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Record added successfully
                                    Toast.makeText(AddRecords.this, "Record Added Successfully", Toast.LENGTH_SHORT).show();
                                    resetInputFields();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Adding record failed
                                    Toast.makeText(AddRecords.this, "Adding Record Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private boolean isValidIdCard(String idCard) {
        // Remove dashes and spaces from the ID card number
        idCard = idCard.replaceAll("-", "").replaceAll(" ", "");

        // Check if the ID card number is 13 digits long
        if (idCard.length() != 13) {
            return false;
        }
        // Check if the ID card contains only digits
        if (!idCard.matches("\\d+")) {
            return false;
        }
        return true;
    }

    private void resetInputFields() {
        nameEditText.setText("");
        idCardEditText.setText("");
        companySpinner.setSelection(0);
        modelSpinner.setSelection(0);
        priceEditText.setText("");
    }
}
