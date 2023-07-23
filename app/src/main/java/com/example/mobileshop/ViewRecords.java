package com.example.mobileshop;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewRecords extends AppCompatActivity {

    private TextView recordsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        recordsTextView = findViewById(R.id.recordsTextView);

        // Retrieve data from Firebase database
        FirebaseDatabase.getInstance().getReference().child("Records")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        StringBuilder recordsText = new StringBuilder();
                        int recordNumber = 1;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            DataItem dataItem = snapshot.getValue(DataItem.class);
                            if (dataItem != null) {
                                recordsText.append("Record Number ").append(recordNumber).append("\n\n")
                                        .append("Name: ").append(dataItem.getName()).append("\n")
                                        .append("ID Card: ").append(dataItem.getIdCard()).append("\n")
                                        .append("Company: ").append(dataItem.getCompany()).append("\n")
                                        .append("Model: ").append(dataItem.getModel()).append("\n")
                                        .append("Price: ").append(dataItem.getPrice()).append(" PKR\n")
                                        .append("=============================== \n\n ");
                            }
                            recordNumber++;
                        }

                        if (recordsText.length() > 0) {
                            recordsTextView.setText(recordsText.toString());
                        } else {
                            Toast.makeText(ViewRecords.this, "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });

    }
}
