package com.smartwebarts.developer.naujyu.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.utils.Toolbar_Set;

public class FormActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        context = this;
        Toolbar_Set.INSTANCE.setToolbar(this, "Submit Repair");
        Toolbar_Set.INSTANCE.setBottomNav(this);
    }
}