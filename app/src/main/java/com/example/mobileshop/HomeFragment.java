package com.example.mobileshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button addRecordsButton = view.findViewById(R.id.btn_add);
        addRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AddRecords activity
                Intent intent = new Intent(getActivity(), AddRecords.class);
                startActivity(intent);
            }
        });

        Button viewRecordsButton = view.findViewById(R.id.btn_view);
        viewRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ViewRecords activity
                Intent intent = new Intent(getActivity(), ViewRecords.class);
                startActivity(intent);
            }
        });

        Button deleteRecordsButton = view.findViewById(R.id.btn_delete);
        deleteRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the DeleteRecords activity
                Intent intent = new Intent(getActivity(), DeleteRecords.class);
                startActivity(intent);
            }
        });

        Button updateRecordsButton = view.findViewById(R.id.btn_update);
        updateRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UpdateRecords activity
                Intent intent = new Intent(getActivity(), UpdateRecords.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
