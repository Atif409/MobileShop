package com.example.mobileshop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteRecords extends AppCompatActivity {

    private EditText idCardEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_records);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records");

        idCardEditText = findViewById(R.id.editTextIdCard);

        Button deleteRecordButton = findViewById(R.id.btnDeleteRecord);
        deleteRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idCard = idCardEditText.getText().toString();

                if (TextUtils.isEmpty(idCard) || idCard.length() != 13 || idCard.contains("-")) {
                    Toast.makeText(DeleteRecords.this, "Please enter a valid ID card number", Toast.LENGTH_SHORT).show();
                } else {
                    deleteRecordFromFirebase(idCard);
                }
            }
        });
    }

    private void deleteRecordFromFirebase(String idCard) {
        databaseReference.orderByChild("idCard").equalTo(idCard).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue();
                        idCardEditText.setText("");
                    }
                    Toast.makeText(DeleteRecords.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DeleteRecords.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull
                                    DatabaseError databaseError) {
                Toast.makeText(DeleteRecords.this, "Failed to delete record: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_all_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuDeleteAll) {
            showConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all records?")
                .setPositiveButton("Yes", (dialog, which) -> deleteAllRecordsFromFirebase())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteAllRecordsFromFirebase() {
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(DeleteRecords.this, "All records deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(DeleteRecords.this, "Failed to delete records: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
