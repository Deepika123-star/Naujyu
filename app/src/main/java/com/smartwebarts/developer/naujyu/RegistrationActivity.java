package com.smartwebarts.developer.naujyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.developer.naujyu.dialog.NothingSelectedSpinnerAdapter;
import com.smartwebarts.developer.naujyu.models.SignUpModel;
import com.smartwebarts.developer.naujyu.models.StateModel;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.sharedpreferences.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private EditText ed_username,ed_email,ed_password, ed_zipcode;
    private Spinner spinnerstate, spinnercity;
    private LinearLayout llcity;
    private String state = "";
    private String city = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        changeStatusBarColor();
        ed_email = findViewById(R.id.ed_mobile);
        ed_username = findViewById(R.id.ed_name);
        ed_password = findViewById(R.id.ed_password);
        ed_zipcode = findViewById(R.id.ed_zipcode);
        spinnerstate = findViewById(R.id.spinnerstate);
        spinnercity = findViewById(R.id.spinnercity);

        llcity = findViewById(R.id.llcity);
        getStateList();
    }

    private void getStateList() {
        Util util = new Util();
        if (util.isNetworkAvialable(this)) {

            UtilMethods.INSTANCE.state(this, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type type = new TypeToken<List<StateModel>>(){}.getType();
                    List <StateModel> statelist = new Gson().fromJson(message, type);
                    ArrayAdapter<StateModel> brandAdapter = new ArrayAdapter(RegistrationActivity.this, R.layout.support_simple_spinner_dropdown_item, statelist);
                    spinnerstate.setPrompt("Select State");
                    spinnerstate.setAdapter(
                            new NothingSelectedSpinnerAdapter(
                                    brandAdapter,
                                    R.layout.contact_spinner_row_nothing_selected_state,
                                    // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                    RegistrationActivity.this));

                    spinnerstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (spinnerstate.getSelectedItem() !=null) {
                                state = ((StateModel) spinnerstate.getSelectedItem()).getId();
                                getCities();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
//                            llBrand.setVisibility(View.VISIBLE);
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(RegistrationActivity.this, from, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            util.showNetworkError(this);
        }

    }

    private void getCities() {
        Util util = new Util();
        if (util.isNetworkAvialable(this)) {

            String statename = spinnerstate.getSelectedItem()!=null?spinnerstate.getSelectedItem().toString():"";
            if (statename.isEmpty()) {
                return;
            }
            UtilMethods.INSTANCE.cities(this, statename,new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type type = new TypeToken<List<StateModel>>(){}.getType();
                    List <StateModel> statelist = new Gson().fromJson(message, type);
                    ArrayAdapter<StateModel> brandAdapter = new ArrayAdapter(RegistrationActivity.this, R.layout.support_simple_spinner_dropdown_item, statelist);
                    spinnercity.setPrompt("Select State");
                    spinnercity.setAdapter(
                            new NothingSelectedSpinnerAdapter(
                                    brandAdapter,
                                    R.layout.contact_spinner_row_nothing_selected_city,
                                    // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                    RegistrationActivity.this));

                    llcity.setVisibility(View.VISIBLE);
                    spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (spinnercity.getSelectedItem()!=null) {
                                city = ((StateModel) spinnercity.getSelectedItem()).getId();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

                @Override
                public void fail(String from) {
                    Toast.makeText(RegistrationActivity.this, from, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            util.showNetworkError(this);
        }

    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void moveToLogin(View view) {
        onBackPressed();
    }

    public void Register(View view) {

        if(ed_username.getText().toString().equals("")){
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if(ed_email.getText().toString().equals("")){
            Toast.makeText(this, "Enter Mobile", Toast.LENGTH_SHORT).show();
        }
        else if(ed_password.getText().toString().equals("")){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(spinnerstate.getSelectedItem() == null){
            Toast.makeText(this, "Select Your State", Toast.LENGTH_LONG).show();
        }
        else if(spinnercity. getSelectedItem() == null){
            Toast.makeText(this, "Select Your City", Toast.LENGTH_LONG).show();
        }
        else if(ed_zipcode. getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Zipcode", Toast.LENGTH_LONG).show();
        }
        else{

            Util util = new Util();
            if (util.isNetworkAvialable(this)) {

                UtilMethods.INSTANCE.signup(this, ed_username.getText().toString(), ed_email.getText().toString(), ed_password.getText().toString(), state, city, ed_zipcode.getText().toString(), new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {

                        Toast.makeText(RegistrationActivity.this, "Sign Up Successfull.", Toast.LENGTH_LONG).show();

                        SignUpModel signUpModel = new Gson().fromJson(message, SignUpModel.class);
                        if (signUpModel!=null && signUpModel.getMessage()!=null && signUpModel.getMessage().equalsIgnoreCase("success")) {

                            AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                            preferences.setLoginDetails(message);

                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                            overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                        }

                    }

                    @Override
                    public void fail(String from) {
                        Toast.makeText(RegistrationActivity.this, from, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                util.showNetworkError(this);
            }
        }

    }


}
