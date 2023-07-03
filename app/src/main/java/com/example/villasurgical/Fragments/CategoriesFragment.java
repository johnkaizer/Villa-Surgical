package com.example.villasurgical.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.villasurgical.Models.ProductsModel;
import com.example.villasurgical.R;
import com.example.villasurgical.databinding.FragmentCategoriesBinding;
import com.example.villasurgical.databinding.FragmentScanBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CategoriesFragment extends Fragment {
    private FragmentCategoriesBinding binding;
    EditText fullNameET, descET;
    ImageView imageV;
    AppCompatButton submitBtn, uploadImageBtn;
    public static final int REQUEST_CODE_IMAGE = 101;
    Uri imageUri;
    boolean isImageAdded = false;
    StorageReference storageRef;
    DatabaseReference dataRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        fullNameET = root.findViewById(R.id.fullname);
        descET = root.findViewById(R.id.desc);
        imageV = root.findViewById(R.id.candidate_image);
        submitBtn = root.findViewById(R.id.submtBtn);
        uploadImageBtn = root.findViewById(R.id.image_btn);
        dataRef = FirebaseDatabase.getInstance().getReference().child("Products");
        storageRef = FirebaseStorage.getInstance().getReference().child("ProductsImages");
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);

            }
        });
        // Set click listener for the submit button
        submitBtn.setOnClickListener(v -> {
            saveProductToFirebase();
        });


        return root;
    }

    private void saveProductToFirebase() {
        String fullName = fullNameET.getText().toString().trim();
        String description = descET.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(description) || !isImageAdded) {
            Toast.makeText(getContext(), "Please fill all the fields and add an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique key for the product
        String productId = dataRef.push().getKey();

        // Create a reference to the product in the database
        DatabaseReference productRef = dataRef.child(productId);

        // Upload the image to Firebase Storage
        StorageReference imageRef = storageRef.child(productId + ".jpg");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Create a new ProductsModel object with the product details
                        ProductsModel product = new ProductsModel(uri.toString(), fullName, description);

                        // Save the product to the database
                        productRef.setValue(product)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Product saved successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the input fields and reset the image
                                    fullNameET.setText("");
                                    descET.setText("");
                                    imageV.setImageResource(R.drawable.pic);
                                    isImageAdded = false;
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save product", Toast.LENGTH_SHORT).show());
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            imageV.setImageURI(imageUri);

        }

    }
}