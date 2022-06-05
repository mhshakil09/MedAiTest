package com.example.medaitest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medaitest.R;
import com.example.medaitest.databinding.ActivityProfileBinding;
import com.example.medaitest.utils.Constant;
import com.example.medaitest.utils.Functions;
import com.example.medaitest.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private JSONObject jsonObject;
    private JsonObjectRequest objRequest;
    private RequestQueue queue;
    private SharedPreferences preferences;

    private ActivityProfileBinding binding;

    private Functions functions;

    String[] countryList = {"Select", "Bangladesh", "India", "Pakistan", "China", "Japan"};
    String[] divisionList = {"Select", "Dhaka", "CTG", "Khulna"};
    String[] districtList = {"Select", "Dhaka", "Narayangonj", "Tangail", "Cumilla", "CTG"};

    private ArrayAdapter<CharSequence> countrySpinnerAdapter;
    private ArrayAdapter<CharSequence> divisionSpinnerAdapter;
    private ArrayAdapter<CharSequence> districtSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initClickListener();
    }

    private void init() {
        jsonObject = new JSONObject();
        queue = Volley.newRequestQueue(this);
        functions = new Functions(this);
        preferences = getSharedPreferences(Constant.LOGIN_PREFERENCE,MODE_PRIVATE);

        fetchCountry();
        fetchDivision();
        fetchDistrict();
    }

    private void initClickListener() {
        binding.submitBtn.setOnClickListener(view -> {
            Helper.toast(getApplicationContext(), "On Progress");
            if (functions.isConnected()) {
                sendToServer();
            } else {
                functions.connection();
            }
        });

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
    }

    private void fetchCountry() {

        countrySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.country_array, R.layout.custom_spinner);
        countrySpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
//        menoSpinner.setAdapter(menoSpinnerAdapter);


//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
//                (this, android.R.layout.simple_spinner_item, countryList);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCountry.setAdapter(countrySpinnerAdapter);
    }

    private void fetchDivision() {
        divisionSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.division_array, R.layout.custom_spinner);
        divisionSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
//                (this, android.R.layout.simple_spinner_item, divisionList);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDivision.setAdapter(divisionSpinnerAdapter);
    }

    private void fetchDistrict() {
        districtSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.district_array, R.layout.custom_spinner);
        districtSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
//                (this, android.R.layout.simple_spinner_item, districtList);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDistrict.setAdapter(districtSpinnerAdapter);
    }

    private void sendToServer(){
        Log.e(TAG, "functionsTop");
        try {

//            jsonObject.put("username",mobile);
//            jsonObject.put("password",password);
            jsonObject.put("isLoggedIn", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, jsonObject.toString());
        functions.showProgressDialog(getString(R.string.loading));

        objRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_EMPLOYEE_LIST, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        functions.hideProgressDialog();
                        //Log.e("OnResponseLogin", String.valueOf(response));
                        //Toast.makeText(APITestActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
//                            token = response.getString("access");
//                            refresh = response.getString("refresh");
//                            Log.e("OnResponseToken", String.valueOf(token));
//                            if(!token.isEmpty()) {
//                                preferences.edit().putString(Constant.TOKEN,token).apply();
//                                preferences.edit().putString(Constant.REFRESH,refresh).apply();
//                                //getUserInfoFromServer(mobile,token);
//                                startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
//                                finish();
//                            } else {
//                                functions.singlePositiveMessage(getString(R.string.failed), getString(R.string.something_wrong), getString(R.string.ok));
//                            }

                            Log.e("OnResponse", "Successful");
                            Log.e("OnResponseBody", response.getString("employees"));
                        } catch (JSONException e) {
                            functions.hideProgressDialog();
                            e.printStackTrace();
                            functions.singlePositiveMessage(getString(R.string.failed),getString(R.string.something_wrong),getString(R.string.ok));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                functions.hideProgressDialog();
                functions.singlePositiveMessage(getString(R.string.failed),getString(R.string.invalid_operator),getString(R.string.ok));
                Log.e("OnErrorLoginUser", String.valueOf(error.toString()));
            }
        });
        queue.add(objRequest);
    }
}