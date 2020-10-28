package com.smartwebarts.developer.naujyu.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartwebarts.developer.naujyu.ContactUsActivity;
import com.smartwebarts.developer.naujyu.LoginActivity;
import com.smartwebarts.developer.naujyu.ProfileActivity;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.ServicesActivity;
import com.smartwebarts.developer.naujyu.sharedpreferences.AppSharedPreferences;

public enum Toolbar_Set {

    INSTANCE;

    public void setToolbar(final Activity activity){
        ImageView back = activity.findViewById(R.id.back);
        ImageView account = activity.findViewById(R.id.account);
        ImageView logout = activity.findViewById(R.id.logout);
        ImageView notification = activity.findViewById(R.id.notification);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        AppSharedPreferences preferences = new AppSharedPreferences(activity.getApplication());

        if (preferences.getLoginUserLoginId().isEmpty()){
            account.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        } else {
            account.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        }
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.logout(activity);
            }
        });


    }

    public void setToolbar(final Activity activity, final String name){
        ImageView back = activity.findViewById(R.id.back);
        ImageView account = activity.findViewById(R.id.account);
        ImageView logout = activity.findViewById(R.id.logout);
        ImageView notification = activity.findViewById(R.id.notification);
        TextView tvName = activity.findViewById(R.id.tv_name);

        
        tvName.setText(name);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        AppSharedPreferences preferences = new AppSharedPreferences(activity.getApplication());

        if (preferences.getLoginUserLoginId().isEmpty()){
            account.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        } else {
            account.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        }
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.logout(activity);
            }
        });

    }


    public void setBottomNav(final Activity activity) {
        final BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);

        BottomNavigationMenuView mbottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        View view = mbottomNavigationMenuView.getChildAt(3);

//        BottomNavigationItemView itemView = (BottomNavigationItemView) view;
//
//        View cart_badge = LayoutInflater.from(activity)
//                .inflate(R.layout.view_alertsbadge,
//                        mbottomNavigationMenuView, false);

//        itemView.addView(cart_badge);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home) {
                    if (!(activity instanceof ServicesActivity)) {
                        activity.startActivity(new Intent(activity, ServicesActivity.class));
                        activity.finishAffinity();
                    }
                } else if (item.getItemId() == R.id.contactus) {
                    if (!(activity instanceof ContactUsActivity)) {
                        activity.startActivity(new Intent(activity, ContactUsActivity.class));
                    }
                } else if (item.getItemId() == R.id.pro) {
                    if (!(activity instanceof ProfileActivity)) {
                        activity.startActivity(new Intent(activity, ProfileActivity.class));
                    }
                }

                bottomNavigationView.setSelected(false);
                return false;
            }
        });
    }

}
