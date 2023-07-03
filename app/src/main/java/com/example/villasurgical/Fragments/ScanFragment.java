package com.example.villasurgical.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.villasurgical.Adapters.ProductAdapter;
import com.example.villasurgical.Models.ProductsModel;
import com.example.villasurgical.R;
import com.example.villasurgical.databinding.FragmentScanBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ScanFragment extends Fragment {
    private FragmentScanBinding binding;
    private RecyclerView productsRv;
    private ProductAdapter productAdapter;
    private ArrayList<ProductsModel> productsModels;
    private DatabaseReference databaseReference;
    private Button scanButton;
    private TextView prodText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize views
        productsRv = root.findViewById(R.id.productsRv);
        scanButton = root.findViewById(R.id.scanbutton);
        prodText = root.findViewById(R.id.textView);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Set click listener for the scan button
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QR code scanning
                IntentIntegrator.forSupportFragment(ScanFragment.this).initiateScan();
            }
        });

        return root;
    }

    // Handle the result of QR code scanning
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String databaseName = result.getContents();
                fetchDataFromFirebase(databaseName);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Fetch data from Firebase based on the scanned database name
    private void fetchDataFromFirebase(String databaseName) {
        DatabaseReference database = databaseReference.child(databaseName);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Data exists in the database
                    productsModels = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ProductsModel product = dataSnapshot.getValue(ProductsModel.class);
                        if (product != null) {
                            productsModels.add(product);
                        }
                    }

                    // Set up RecyclerView and adapter
                    productAdapter = new ProductAdapter(productsModels);
                    productsRv.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Use GridLayout with 2 columns
                    productsRv.setAdapter(productAdapter);
                } else {
                    // Database does not exist or has no data
                    prodText.setText("Database not found or empty.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                prodText.setText("Error fetching data from Firebase.");
            }
        });
    }
}
