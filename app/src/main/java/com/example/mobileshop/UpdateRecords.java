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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UpdateRecords extends AppCompatActivity {

    private EditText idCardEditText;
    private EditText nameEditText;
    private Spinner companySpinner;
    private Spinner modelSpinner;
    private EditText priceEditText;
    private Button searchButton;
    private Button updateButton;

    private DatabaseReference databaseReference;
    private String recordKey;

    private static final String[] MOBILE_COMPANIES = {
            "ANDROID",
            "IPHONE"
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_records);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records");

        idCardEditText = findViewById(R.id.editTextIdCard);
        nameEditText = findViewById(R.id.editTextName);
        companySpinner = findViewById(R.id.spinnerMobileCompany);
        modelSpinner = findViewById(R.id.spinnerMobileModel);
        priceEditText = findViewById(R.id.editTextPrice);
        searchButton = findViewById(R.id.btnSearch);
        updateButton = findViewById(R.id.btnUpdateRecord);

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
                    ArrayAdapter<String> androidModelAdapter = new ArrayAdapter<>(UpdateRecords.this,
                            android.R.layout.simple_spinner_item, ANDROID_MODELS);
                    modelSpinner.setAdapter(androidModelAdapter);
                    modelSpinner.setEnabled(true);
                    priceEditText.setEnabled(true);
                    priceEditText.setText(String.valueOf(ANDROID_PRICES[0])); // Set the default price
                } else if (position == 2) {
                    ArrayAdapter<String> iPhoneModelAdapter = new ArrayAdapter<>(UpdateRecords.this,
                            android.R.layout.simple_spinner_item, IPHONE_MODELS);
                    modelSpinner.setAdapter(iPhoneModelAdapter);
                    modelSpinner.setEnabled(true);
                    priceEditText.setEnabled(true);
                    priceEditText.setText(String.valueOf(IPHONE_PRICES[0])); // Set the default price
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idCard = idCardEditText.getText().toString().trim();

                if (idCard.isEmpty()) {
                    Toast.makeText(UpdateRecords.this, "Please enter the ID card number", Toast.LENGTH_SHORT).show();
                } else if (!isValidIdCard(idCard)) {
                    Toast.makeText(UpdateRecords.this, "Please enter a valid ID card number", Toast.LENGTH_SHORT).show();
                } else {
                    searchRecord(idCard);
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecord();
            }
        });
    }

    private boolean isValidIdCard(String idCard) {
        return idCard.length() == 13 && idCard.matches("\\d+");
    }

    private void searchRecord(final String idCard) {
        Query query = databaseReference.orderByChild("idCard").equalTo(idCard);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot recordSnapshot = dataSnapshot.getChildren().iterator().next();
                    recordKey = recordSnapshot.getKey();

                    UpRecord record = recordSnapshot.getValue(UpRecord.class);
                    displayRecord(record);
                } else {
                    Toast.makeText(UpdateRecords.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateRecords.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRecord(UpRecord record) {
        idCardEditText.setText(record.getIdCard());
        nameEditText.setText(record.getName());

        String company = record.getCompany();
        int companyIndex = getIndexFromArray(MOBILE_COMPANIES, company);
        companySpinner.setSelection(companyIndex);

        int modelIndex;
        int[] prices;

        if (company.equals("ANDROID")) {
            modelIndex = getIndexFromArray(ANDROID_MODELS, record.getModel());
            prices = ANDROID_PRICES;
        } else {
            modelIndex = getIndexFromArray(IPHONE_MODELS, record.getModel());
            prices = IPHONE_PRICES;
        }

        modelSpinner.setSelection(modelIndex);
        priceEditText.setText(String.valueOf(prices[modelIndex]));
    }

    private int getIndexFromArray(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private void updateRecord() {
        String idCard = idCardEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String company = companySpinner.getSelectedItem().toString();
        String model = modelSpinner.getSelectedItem().toString();
        String price = priceEditText.getText().toString().trim();

        if (idCard.isEmpty() || name.isEmpty() || price.isEmpty()) {
            Toast.makeText(UpdateRecords.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else {
            // Create a new record object with the updated values
            UpRecord record = new UpRecord(company, model, name, idCard, price);

            // Update the record in Firebase
            DatabaseReference recordRef = databaseReference.child(recordKey);
            recordRef.setValue(record)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdateRecords.this, "Record updated successfully", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateRecords.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearFields() {
        idCardEditText.setText("");
        nameEditText.setText("");
        priceEditText.setText("");
        companySpinner.setSelection(0);
        modelSpinner.setSelection(0);
    }

    private void updateModelSpinner(int position) {
        String company = MOBILE_COMPANIES[position];
        ArrayAdapter<String> modelAdapter;

        if (company.equals("ANDROID")) {
            modelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ANDROID_MODELS);
        } else if (company.equals("IPHONE")) {
            modelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, IPHONE_MODELS);
        } else {
            // Handle invalid company case here
            return;
        }

        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelAdapter);
    }

}
