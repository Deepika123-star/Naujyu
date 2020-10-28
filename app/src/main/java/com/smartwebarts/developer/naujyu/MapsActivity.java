package com.smartwebarts.developer.naujyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.developer.naujyu.dialog.RequestDialog;
import com.smartwebarts.developer.naujyu.models.VehicleModel;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.sharedpreferences.AppSharedPreferences;
import com.smartwebarts.developer.naujyu.utils.LocationAddress;
import com.smartwebarts.developer.naujyu.utils.Toolbar_Set;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public static MapsActivity instance = null;
    private GoogleMap mMap;
    GPSTracker gpsTracker;
    private double mLat;
    private double mLang;
    private boolean doubleBackToExitPressedOnce = false;
    private List<VehicleModel> vehicleList;
    private String strlocation;
    private LocationAddress locationAddress;
//    private DrawerLayout drawer;
//    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_dashboard);

        gpsTracker = new GPSTracker(this);
        locationAddress = new LocationAddress();
        gpsTracker.getLocation();

        Toolbar_Set.INSTANCE.setToolbar(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
//
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        setNavigationDrawer();
//
//        navigationView.setNavigationItemSelectedListener(this);

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            UtilMethods.INSTANCE.vehicletype(this, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    if (!message.isEmpty()) {
                        Type listType = new TypeToken<ArrayList<VehicleModel>>(){}.getType();
                        vehicleList = new Gson().fromJson(message, listType);
                    }
                }

                @Override
                public void fail(String from) {

                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

//    private void setNavigationDrawer() {
//        View headerLayout = navigationView.getHeaderView(0);
//        Menu nav_Menu = navigationView.getMenu();
//        TextView tvUser = headerLayout.findViewById(R.id.tvName);
//        TextView tvEmail = headerLayout.findViewById(R.id.tvEmail);
//
//        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
//
//        if (preferences.getLoginUserName()!=null && !preferences.getLoginUserName().isEmpty()) {
//            String[] s = preferences.getLoginUserName().trim().split("\\s+");
//            tvUser.setText("Welcome " +s[0]);
//        } else {
//            nav_Menu.findItem(R.id.nav_home).setVisible(false);
//            nav_Menu.findItem(R.id.logout).setVisible(false);
//        }
//
//        if (preferences.getLoginMobile()!=null && !preferences.getLoginMobile().isEmpty()) {
//            tvEmail.setText("" +preferences.getLoginMobile());
//        } else {
//            tvEmail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
//                }
//            });
//        }
//
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        if (gpsTracker.canGetLocation()){

            mLat = gpsTracker.getLatitude();
            mLang = gpsTracker.getLongitude();

            locationAddress.getAddressFromLocation(mLat, mLang,
                    getApplicationContext(), new GeocoderHandler());

        } else {
            gpsTracker.showSettingsAlert();
        }

    }

    private void addMarker() {
        if (mMap!=null) {
            LatLng sydney = new LatLng(mLat, mLang);
            mMap.addMarker(new MarkerOptions().position(sydney).title(""+strlocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20));
        }
    }

    public void sos(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:8873101608"));
        startActivity(intent);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//        switch (menuItem.getItemId()) {
//            case R.id.nav_home : {
//                startActivity(new Intent(MapsActivity.this, ServicesActivity.class));
//                break;
//            }
//            case R.id.logout : {
//                logout();
//                break;
//            }
//        }
//        drawer.closeDrawer(GravityCompat.START);
//        return false;
//    }

//    public void openDrawer(View view) {
//        drawer.openDrawer(GravityCompat.START);
//    }

    private void logout() {
        AppSharedPreferences preferences = new AppSharedPreferences(MapsActivity.this.getApplication());
        preferences.logout(MapsActivity.this);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            strlocation = locationAddress;
            addMarker();
//            placesAutocomplete.setText(locationAddress);
        }
    }

    public void openDialog(View view) {
        RequestDialog requestDialog = new RequestDialog(this, vehicleList, strlocation, ""+mLat, ""+mLang);
        requestDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        instance = null;
    }

    @Override
    public void onMapClick(LatLng latLng) {

//        mLat = latLng.latitude;
//        mLang = latLng.longitude;
//        locationAddress.getAddressFromLocation(mLat, mLang,
//                getApplicationContext(), new GeocoderHandler());
    }
}
