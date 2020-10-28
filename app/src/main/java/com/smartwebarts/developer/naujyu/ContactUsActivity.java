package com.smartwebarts.developer.naujyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.smartwebarts.developer.naujyu.contactsmap.MapsActivity;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void MapActivity(View view) {

        Intent intent=new Intent(ContactUsActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}