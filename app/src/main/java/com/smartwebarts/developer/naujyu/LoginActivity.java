package com.smartwebarts.developer.naujyu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smartwebarts.developer.naujyu.models.LoginData;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.sharedpreferences.AppSharedPreferences;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 105;
    EditText ed_email,ed_password;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_login);
        ed_email = findViewById(R.id.ed_mobile);
        ed_password = findViewById(R.id.ed_password);


        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        String user = preferences.getLoginDetails();
        if (user!= null && !user.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, ServicesActivity.class);
            finish();
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(intent);
        }

        checkAppPermissions();
    }

    public void Login(View view) {

        if(ed_email.getText().toString().equals("")){
            Toast.makeText(this, "Enter Mobile", Toast.LENGTH_SHORT).show();
        }
        else if(ed_password.getText().toString().equals("")){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else{
//            Toast.makeText(this, "Login Click", Toast.LENGTH_SHORT).show();
            Util util = new Util();
            if (util.isNetworkAvialable(this)) {

                UtilMethods.INSTANCE.Login(this, ed_email.getText().toString(), ed_password.getText().toString(), new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {

                        LoginData data = new Gson().fromJson(message, LoginData.class);

                        if (data.getMessage()!=null && data.getMessage().equalsIgnoreCase("success")){
                            AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                            preferences.setLoginDetails(message);
                            startActivity();
                        }
                    }

                    @Override
                    public void fail(String from) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText(""+from)
                                .show();
                    }
                });
            } else {
                util.showNetworkError(this);
            }

        }
    }

    public void startActivity(){
        startActivity(new Intent(this,ServicesActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
//        finish();
    }

    public void checkAppPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions((Activity) this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions((Activity) this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(
                new Runnable()
                {

                    @Override
                    public void run()
                    {
                        doubleBackToExitPressedOnce=false;
                    }
                },
                2000
        );
    }
//
//    public void skip(View view) {
//        startActivity();
//    }
}
