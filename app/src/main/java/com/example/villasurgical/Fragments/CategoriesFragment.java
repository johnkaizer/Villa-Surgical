package com.example.villasurgical.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.villasurgical.Adapters.CategoryAdapter;
import com.example.villasurgical.Models.CategoryModel;
import com.example.villasurgical.Models.ProductsModel;
import com.example.villasurgical.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<CategoryModel> categoryModels;
    CategoryAdapter categoryAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView =rootView.findViewById(R.id.categoriesRv);

        categoryModels =new ArrayList<>();

        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));
        categoryModels.add(new CategoryModel("Test Category"));



        categoryAdapter = new CategoryAdapter(getActivity(), categoryModels, this);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);



        return rootView;
    }

}