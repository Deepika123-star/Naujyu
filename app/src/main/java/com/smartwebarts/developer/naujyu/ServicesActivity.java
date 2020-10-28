package com.smartwebarts.developer.naujyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.smartwebarts.developer.naujyu.models.ServiceList;
import com.smartwebarts.developer.naujyu.utils.CustomSlider;
import com.smartwebarts.developer.naujyu.utils.LocationAddress;
import com.smartwebarts.developer.naujyu.utils.Toolbar_Set;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private static final int REQUEST_LOCATION = 202;
    RecyclerView recyclerView;
    private SliderLayout home_list_banner;
    private boolean doubleBackToExitPressedOnce;
    private GPSTracker gpsTracker;
    private LocationAddress locationAddress;
    private double mLat, mLang;
    private GoogleApiClient googleApiClient;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Toolbar_Set.INSTANCE.setToolbar(this);
        Toolbar_Set.INSTANCE.setBottomNav(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        home_list_banner =  (SliderLayout) findViewById(R.id.home_img_slider);
         textView = findViewById(R.id.tv_name);
         textView.setText(R.string.app_name);

        HomeAdapter adapter = new HomeAdapter(this, ServiceList.getServiceList(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        gpsTracker = new GPSTracker(this);
        locationAddress = new LocationAddress();
        gpsTracker.getLocation();

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.slider1);
        list.add(R.drawable.slider2);
        list.add(R.drawable.slider3);
        setSlider(list);

        checkPermission();
    }

    private void checkPermission() {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // You can use the API that requires the permission.
            turnongps();
        }  else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    private void turnongps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
//            Toast.makeText(getApplicationContext(),"Gps already enabled",Toast.LENGTH_SHORT).show();
            //getActivity().finish();
        }
        // Todo Location Already on  ... end

        if(!hasGPSDevice(this)){
//            Toast.makeText(getApplicationContext(),"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            // Log.e("Neha","Gps already enabled");
//            Toast.makeText(getApplicationContext(),"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        }else{
            // Log.e("Neha","Gps already enabled");
//            Toast.makeText(getApplicationContext(),"Gps already enabled",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(7 * 1000);  //30 * 1000
            locationRequest.setFastestInterval(5 * 1000); //5 * 1000
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient,builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(ServicesActivity.this,REQUEST_LOCATION);

                                // getActivity().finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    private void setSlider(List<Integer> list) {

        for (int data : list) {
            CustomSlider textSliderView = new CustomSlider(this);
            // initialize a SliderLayout
            textSliderView
                    .image(data)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            home_list_banner.addSlider(textSliderView);
        }

        addAddress();
    }

    private void addAddress() {
        if (gpsTracker.canGetLocation()){

            mLat = gpsTracker.getLatitude();
            mLang = gpsTracker.getLongitude();

            locationAddress.getAddressFromLocation(mLat, mLang,
                    getApplicationContext(), new GeocoderHandler());

        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

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
            String strlocation = locationAddress;
            TextView textView = findViewById(R.id.tv_name);
            if (strlocation!=null && !strlocation.isEmpty()) {
                textView.setText(strlocation);
            }
//            placesAutocomplete.setText(locationAddress);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    turnongps();
                }  else {
                    turnongps();
                }
                return;
        }
    }

}