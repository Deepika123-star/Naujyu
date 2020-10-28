package com.smartwebarts.developer.naujyu;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.developer.naujyu.models.MessageModel;
import com.smartwebarts.developer.naujyu.models.StateModel;
import com.smartwebarts.developer.naujyu.models.VendorDetails;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.sharedpreferences.AppSharedPreferences;
import com.smartwebarts.developer.naujyu.utils.LocationAddress;
import com.smartwebarts.developer.naujyu.utils.Toolbar_Set;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 202;
    private boolean doubleBackToExitPressedOnce;
    private GPSTracker gpsTracker;
    private LocationAddress locationAddress;
    private double mLat, mLang;
    private GoogleApiClient googleApiClient;
    private TextInputEditText etName, etMobile, etLocation, etZipcode, /*etShop, etGstin,*/ etPassword;
    private TextView name;
//    private ImageView shopImage, adharcard, addressImage;
//    private TextView btnshopImage, btnadharcard, btnaddressImage;
    private Button save;
    private Uri aadharUri, shopUri, addressUri;
    private List<StateModel> states;
    private List<StateModel> cities;
    private Spinner spinnerstate, spinnercity;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ArrayAdapter<StateModel> stateAdapter, cityAdapter;
    private VendorDetails details;
    private AppSharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar_Set.INSTANCE.setToolbar(this);
//        Toolbar_Set.INSTANCE.setBottomNav(this);
        preferences = new AppSharedPreferences(getApplication());

        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
//        etShop = findViewById(R.id.etShop);
        etLocation = findViewById(R.id.etLocation);
        etZipcode = findViewById(R.id.etZipcode);
//        etGstin = findViewById(R.id.etGstin);
        etPassword = findViewById(R.id.etPassword);
        spinnerstate = findViewById(R.id.spinnerstate);
        spinnercity = findViewById(R.id.spinnercity);

        name = findViewById(R.id.name);
//        shopImage = findViewById(R.id.shopImage);
//        adharcard = findViewById(R.id.adharcard);
//        addressImage = findViewById(R.id.addressImage);
//        btnshopImage = findViewById(R.id.btnUploadShop);
//        btnadharcard = findViewById(R.id.btnUploadAadhar);
//        btnaddressImage = findViewById(R.id.btnUploadAddress);
        save = findViewById(R.id.save);

        gpsTracker = new GPSTracker(this);
        locationAddress = new LocationAddress();
        gpsTracker.getLocation();

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.slider1);
        list.add(R.drawable.slider2);
        list.add(R.drawable.slider3);
        setDisable(null);
        getDetails();
        checkPermission();
    }

    private void getDetails() {

        String id = preferences.getLoginUserLoginId();
        if (id!=null && !id.isEmpty()) {
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                UtilMethods.INSTANCE.loginDetails(this, id, new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {

                        details = new Gson().fromJson(message, VendorDetails.class);
                        if (details != null) {
                            etName.setText(details.getName());
//                            etShop.setText(details.getShopname());
                            etLocation.setText(details.getLocation());
                            name.setText(details.getStatus());
                            etZipcode.setText(details.getZipcode());
//                            Picasso.with(ProfileActivity.this)
//                                    .load(APIClient.BASE_URL+details.getShopimage())
//                                    .placeholder(R.drawable.store)
//                                    .error(R.drawable.store)
//                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                                    .into(shopImage);
//
//                            Picasso.with(ProfileActivity.this)
//                                    .load(APIClient.BASE_URL+details.getAadharcard())
//                                    .placeholder(R.drawable.adhaar)
//                                    .error(R.drawable.adhaar)
//                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                                    .into(adharcard);
//
//                            Picasso.with(ProfileActivity.this)
//                                    .load(APIClient.BASE_URL+details.getAddressverification())
//                                    .placeholder(R.drawable.addressproof)
//                                    .error(R.drawable.addressproof)
//                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                                    .into(addressImage);
//
//                            if (details.getStatus()!=null && details.getStatus().equalsIgnoreCase("verified")) {
//                                btnshopImage.setVisibility(View.GONE);
//                                btnadharcard.setVisibility(View.GONE);
//                            }
//                            etGstin.setText(details.getGstin());

                            etPassword.setText(details.getPassword());
                            etMobile.setText(details.getContact());
                            getStateList();

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
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this,permissions,101);
            } else {
                ActivityCompat.requestPermissions(this,permissions,101);
            }
            return;
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
                                status.startResolutionForResult(ProfileActivity.this,REQUEST_LOCATION);

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

    public void setEnable(View view) {
//        getStateList();
        etName.setEnabled(true);
        spinnerstate.setEnabled(true);
        etLocation.setEnabled(true);
        etMobile.setEnabled(true);
//        etShop.setEnabled(true);
        etZipcode.setEnabled(true);
//        etGstin.setEnabled(true);
        spinnercity.setEnabled(true);
        etPassword.setEnabled(true);
        save.setEnabled(true);
        etName.requestFocus();
        etName.setSelectAllOnFocus(true);
        etName.selectAll();
    }

    private void getStateList() {

        Util util = new Util();
        if (util.isNetworkAvialable(this)) {

            UtilMethods.INSTANCE.state(ProfileActivity.this, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type type = new TypeToken<List<StateModel>>(){}.getType();
                    List <StateModel> statelist = new Gson().fromJson(message, type);
                    stateAdapter = new ArrayAdapter(ProfileActivity.this, R.layout.support_simple_spinner_dropdown_item, statelist);
                    spinnerstate.setAdapter(stateAdapter);

                    if (details!=null) {
                        for (int position = 0; position < stateAdapter.getCount(); position++) {
                            if(stateAdapter.getItem(position).toString().equalsIgnoreCase(details.getStatename())) {
                                spinnerstate.setSelection(position);
                                getCities(stateAdapter.getItem(position));
                                return;
                            }
                        }
                    }

                    spinnerstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            getCities2(null);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(ProfileActivity.this, from, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            util.showNetworkError(this);
        }
    }

    private void getCities2(StateModel item) {
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
                    cityAdapter = new ArrayAdapter(ProfileActivity.this, R.layout.support_simple_spinner_dropdown_item, statelist);
                    spinnercity.setAdapter(cityAdapter);
                    spinnercity.setSelection(0);
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(ProfileActivity.this, from, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            util.showNetworkError(this);
        }

    }


    private void getCities(StateModel item) {
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
                    cityAdapter = new ArrayAdapter(ProfileActivity.this, R.layout.support_simple_spinner_dropdown_item, statelist);
                    spinnercity.setAdapter(cityAdapter);
                    if (details!=null) {
                        for (int position = 0; position < cityAdapter.getCount(); position++) {
//                            Log.e("test", cityAdapter.getItem(position).toString()+","+details.getCityname());
                            if(cityAdapter.getItem(position).toString().trim().equalsIgnoreCase(details.getCityname().trim())) {
                                spinnercity.setSelection(position);
                                return;
                            }
                        }
                    }
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(ProfileActivity.this, from, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            util.showNetworkError(this);
        }

    }

    public void setDisable(View view) {
        etName.setEnabled(false);
        spinnerstate.setEnabled(false);
        etLocation.setEnabled(false);
        etMobile.setEnabled(false);
//        etShop.setEnabled(false);
        etZipcode.setEnabled(false);
//        etGstin.setEnabled(false);
        etPassword.setEnabled(false);
        spinnercity.setEnabled(false);
        save.setEnabled(false);
    }

//    public void openGallery(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        if (view == btnshopImage)
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//        if (view == btnadharcard)
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
//        if (view == btnaddressImage)
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
//    }

    public void save(View view) {
        if (verifyForm()) {
            setDisable(view);
            UtilMethods.INSTANCE.uploadProfile(this,
                    preferences.getLoginUserLoginId(),
                    etName.getText().toString(),
                    etMobile.getText().toString(),
                    etPassword.getText().toString(),
//                    etShop.getText().toString(),
                    "",
                    etLocation.getText().toString(),
                    ((StateModel) spinnerstate.getSelectedItem()).getId(),
                    ((StateModel) spinnercity.getSelectedItem()).getId(),
                    etZipcode.getText().toString(),
//                    etGstin.getText().toString(),
                    "",
                    new mCallBackResponse() {
                        @Override
                        public void success(String from, String message) {
                            MessageModel model = new Gson().fromJson(message, MessageModel.class);
                            new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Update")
                                    .setContentText(""+model.getMessage())
                                    .show();
                        }

                        @Override
                        public void fail(String from) {
                            new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Update")
                                    .setContentText("Successfull")
                                    .show();
                        }
                    });
        }
    }

    private boolean verifyForm() {

        if (etName.getText().toString().trim().isEmpty()){
            etName.setError("Name required");
            return false;
        } else if (etMobile.getText().toString().trim().isEmpty() || etMobile.getText().toString().trim().length()<10){
            etMobile.setError("10 digits Mobile required");
            return false;
        }
//        else if (etShop.getText().toString().trim().isEmpty()){
//            etShop.setError("Shop Name required");
//            return false;
//        } else if (etGstin.getText().toString().trim().isEmpty() || etGstin.getText().toString().trim().length()<15){
//            etGstin.setError("15 digits gstin required");
//            return false;
//        }
        else if (etLocation.getText().toString().trim().isEmpty()){
            etLocation.setError("Location required");
            return false;
        } else if (etZipcode.getText().toString().trim().isEmpty()){
            etZipcode.setError("Zipcode required");
            return false;
        }
        return true;
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
            textView.setText(strlocation);
//            placesAutocomplete.setText(locationAddress);
        }
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && null != data) {
//
//            Uri selectedImage = data.getData();
//            String path = selectedImage.getPath();
//            switch (requestCode) {
//                case 1 :
//                    shopImage.setImageURI(selectedImage);
//                    aadharUri = selectedImage;
//                    InputStream is = null;
//                    try {
//                        is = getContentResolver().openInputStream(data.getData());
//                        uploadShopImage(getBytes(is));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case 2 :
//                    adharcard.setImageURI(selectedImage);
//                    shopUri = selectedImage;
//                    try {
//                        is = getContentResolver().openInputStream(data.getData());
//                        uploadAdharImage(getBytes(is));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case 3 :
//                    addressImage.setImageURI(selectedImage);
//                    addressUri = selectedImage;
//                    try {
//                        is = getContentResolver().openInputStream(data.getData());
//                        uploadAddressImage(getBytes(is));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//
//        }
//    }

//    private void uploadAddressImage(byte[] imageBytes) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("addressverification", "image.jpg", requestFile);
//
//        String id = preferences.getLoginUserLoginId();
//        UtilMethods.INSTANCE.uploadImage(this, id, null, null,null, null, body, requestFile,new mCallBackResponse() {
//            @Override
//            public void success(String from, String message) {
//                MessageModel model = new Gson().fromJson(message, MessageModel.class);
//                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Update")
//                        .setContentText(""+model.getMessage())
//                        .show();
//            }
//
//            @Override
//            public void fail(String from) {
//                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Update")
//                        .setContentText(""+from)
//                        .show();
//            }
//        });
//    }

//    private void uploadShopImage(byte[] imageBytes) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("aadhar", "image.jpg", requestFile);
//
//        String id = preferences.getLoginUserLoginId();
//        UtilMethods.INSTANCE.uploadImage(this, id, body, requestFile,null, null, null, null, new mCallBackResponse() {
//            @Override
//            public void success(String from, String message) {
//                MessageModel model = new Gson().fromJson(message, MessageModel.class);
//                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Update")
//                        .setContentText(""+model.getMessage())
//                        .show();
//            }
//
//            @Override
//            public void fail(String from) {
//                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Update")
//                        .setContentText(""+from)
//                        .show();
//            }
//        });
//    }
//    private void uploadAdharImage(byte[] imageBytes) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("aadhar", "aadhar.jpg", requestFile);
//
//        String id = preferences.getLoginUserLoginId();
//        UtilMethods.INSTANCE.uploadImage(this, id, null, null, body, requestFile,null, null, new mCallBackResponse() {
//            @Override
//            public void success(String from, String message) {
//                MessageModel model = new Gson().fromJson(message, MessageModel.class);
//                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Update")
//                        .setContentText(""+model.getMessage())
//                        .show();            }
//
//            @Override
//            public void fail(String from) {
//                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Update")
//                        .setContentText(""+from)
//                        .show();              }
//        });
//    }

//    public byte[] getBytes(InputStream is) throws IOException {
//        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
//
//        int buffSize = 1024;
//        byte[] buff = new byte[buffSize];
//
//        int len = 0;
//        while ((len = is.read(buff)) != -1) {
//            byteBuff.write(buff, 0, len);
//        }
//
//        return byteBuff.toByteArray();
//    }
}