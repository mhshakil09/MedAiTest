package com.example.medaitest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.medaitest.databinding.ActivityProfileBinding;
import com.example.medaitest.utils.Helper;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    String[] countryList = {"Select", "Bangladesh", "India", "Pakistan", "China", "Japan"};
    String[] divisionList = {"Select", "Dhaka", "CTG", "Khulna"};
    String[] districtList = {"Select", "Dhaka", "Narayangonj", "Tangail", "Cumilla", "CTG"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initClickListener();
    }

    private void init() {
        fetchCountry();
        fetchDivision();
        fetchDistrict();
    }

    private void initClickListener() {
        binding.submitBtn.setOnClickListener(view -> {
            Helper.toast(getApplicationContext(), "On Progress");
        });

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
    }

    private void fetchCountry() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, countryList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCountry.setAdapter(spinnerArrayAdapter);
    }

    private void fetchDivision() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, divisionList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDivision.setAdapter(spinnerArrayAdapter);
    }

    private void fetchDistrict() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, districtList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDistrict.setAdapter(spinnerArrayAdapter);
    }
}