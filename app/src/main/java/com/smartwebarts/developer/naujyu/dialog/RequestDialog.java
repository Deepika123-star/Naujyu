package com.smartwebarts.developer.naujyu.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.AppCompatTextView;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.smartwebarts.developer.naujyu.MapsActivity;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.models.BrandModel;
import com.smartwebarts.developer.naujyu.models.VehicleModel;
import com.smartwebarts.developer.naujyu.retrofit.BookingResponse;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.sharedpreferences.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RequestDialog extends AlertDialog.Builder {

    private PlacesClient placesClient;
    private String apiKey;
    private AlertDialog dialog;
    private Context context;

    private String vehiclenumber, vehicletype, vehiclemodel, fueltype, location, latitude, longitude, pincode, request="1";

    private Spinner spinVehicle, spinBrand, spinModel, spinFuel;
    private TextInputEditText etVehicleNumber;
//    private TextInputEditText issuedate;
    private AppCompatEditText etPurpose;
    private AppCompatTextView tvLocation;
    private LinearLayout llModel, llBrand;
    private View view;
    private PlacesAutocompleteTextView placesAutocomplete;
    private ArrayAdapter vehicleAdapter, brandAdapter, modelAdapter,fuelAdapter;
    private List<VehicleModel> vehicleList;
    private List<BrandModel> brandList;
    private List<BrandModel> modelList;
//    final Calendar myCalendar = Calendar.getInstance();
//    String myFormat = "dd/MM/yyyy"; //In which you need put here
//    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    public RequestDialog(@NonNull Context context, List<VehicleModel> vehicleList, String location, String latitude, String longitude) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_request, null);


        setCancelable(false);
        dialog = this.create();
        dialog.setCanceledOnTouchOutside(false);

        llModel = view.findViewById(R.id.ll_model);
        llBrand = view.findViewById(R.id.ll_brand);
        spinVehicle = view.findViewById(R.id.vehicletype);
        spinBrand = view.findViewById(R.id.brandtype);
        spinModel = view.findViewById(R.id.modeltype);
        spinFuel = view.findViewById(R.id.fueltype);
        etPurpose = view.findViewById(R.id.etPurpose);
        etVehicleNumber = view.findViewById(R.id.etVehicleNumber);
//        issuedate = view.findViewById(R.id.issuedate);
        tvLocation = view.findViewById(R.id.tv_location_picker);

        this.vehicleList = vehicleList;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        apiKey = context.getString(R.string.location_api_key_id);

        if (!Places.isInitialized()) {
            Places.initialize(context.getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(context);
        placesAutocomplete = view.findViewById(R.id.places_autocomplete);
        placesAutocomplete.setText(location);
        tvLocation.setText(location);

        vehicleAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, vehicleList);
        spinVehicle.setPrompt("Select Vehicle Type");
        spinVehicle.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        vehicleAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        context));


        fuelAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, context.getResources().getStringArray(R.array.fueltypes));
        spinFuel.setPrompt("Select Fuel");
        spinFuel.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        fuelAdapter,
                        R.layout.contact_spinner_row_nothing_selected_fuel,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        context));

        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dia, int which) {
                requestSupport(dialog);
            }
        });
        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        addListeners();
    }

    private void requestSupport(AlertDialog dialog) {

        if (validateform()) {
            if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                AppSharedPreferences preferences = new AppSharedPreferences(((MapsActivity) context).getApplication());
                String customerId = preferences.getLoginUserLoginId();
                if (customerId.isEmpty()){
                    Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }
                String vehicleNumber = etVehicleNumber.getText().toString();
                String vehicleType = ((VehicleModel) spinVehicle.getSelectedItem()).getId();
                String vehicleBrand = ((BrandModel) spinBrand.getSelectedItem()).getId();
                String vehicleModel = ((BrandModel) spinModel.getSelectedItem()).getId();
                String fuelType = spinFuel.getSelectedItem().toString();
                String issue = etPurpose.getText().toString();
                String slocation = getLocation();
                String slatitude = getLatitude();
                String slongitude = getLongitude();
                String spincode = getPincode();

                Log.e("request", customerId+" "+ vehicleNumber+" "+ vehicleType+" "+
                        vehicleModel+" "+ fuelType+" "+ slocation+ " "+slatitude+ " "+ " "+issue+
                        slongitude+" "+ spincode+" "+ vehicleBrand);

                UtilMethods.INSTANCE.booking(context,
                        customerId, vehicleNumber, vehicleType, vehicleModel, fuelType, slocation, slatitude, slongitude, spincode, vehicleBrand, issue,new mCallBackResponse() {
                            @Override
                            public void success(String from, String message) {

                                BookingResponse response = new Gson().fromJson(message, BookingResponse.class);
                                if (response.getMessage().equalsIgnoreCase("success")) {
                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Support Request")
                                            .setContentText(""+response.getMessage())
                                            .show();
                                } else {
                                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Support Request")
                                            .setContentText(""+response.getMessage())
                                            .setConfirmText("OK")
                                            .show();
                                }
                            }

                            @Override
                            public void fail(String from) {

                            }
                        });
            } else {
                UtilMethods.INSTANCE.internetNotAvailableMessage(context);
            }
        }
    }

    private boolean validateform() {
        if (spinVehicle.getSelectedItem() == null) {
            Toast.makeText(context, "Select Vehicle", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinBrand.getSelectedItem() == null) {
            Toast.makeText(context, "Select Brand", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinModel.getSelectedItem() == null) {
            Toast.makeText(context, "Select Fuel", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinFuel.getSelectedItem() == null) {
            Toast.makeText(context, "Select Fuel", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etVehicleNumber.getText().toString().trim().isEmpty()) {
            etVehicleNumber.setError("Enter Vehicle Number");
            return false;
        }
        if (etPurpose.getText().toString().trim().isEmpty()) {
            etPurpose.setError("Enter Reason for Request");
            return false;
        }

//        if (issuedate.getText().toString().trim().isEmpty()) {
//            Toast.makeText(context, "Select a Issue Date", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        
        if (placesAutocomplete.getText().toString().trim().isEmpty()) {
            placesAutocomplete.setError("Enter Address");
            return false;
        }

        return true;
    }

    @Override
    public AlertDialog show() {
        setView(view);
        return super.show();
    }


    private void addListeners() {

//        issuedate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.date_picker);
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                final DatePicker datePicker= dialog.findViewById(R.id.date_picker);
//                Button done= dialog.findViewById(R.id.done);
//                done.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int day= datePicker.getDayOfMonth();
//                        int month= datePicker.getMonth();
//                        int year= datePicker.getYear();
//                        myCalendar.set(year,month,day);
//
//                        issuedate.setText(sdf.format(myCalendar.getTime()));
//                        Log.e("dob",sdf.format(myCalendar.getTime()));
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });
        spinVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                VehicleModel vehicle = (VehicleModel) parent.getSelectedItem();
                if (vehicle != null) {
                    setVehicletype(vehicle.getId());
                    if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                        UtilMethods.INSTANCE.brand(context, vehicle.getId(), new mCallBackResponse() {
                            @Override
                            public void success(String from, String message) {
                                Type type = new TypeToken<List<BrandModel>>(){}.getType();
                                brandList = new Gson().fromJson(message, type);
                                brandAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, brandList);
                                spinBrand.setPrompt("Select Vehicle Brand");
                                spinBrand.setAdapter(
                                        new NothingSelectedSpinnerAdapter(
                                                brandAdapter,
                                                R.layout.contact_spinner_row_nothing_selected_brand,
                                                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                                context));

                                llBrand.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void fail(String from) {

                            }
                        });

                    } else {

                        UtilMethods.INSTANCE.internetNotAvailableMessage(context);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                BrandModel vehicle = (BrandModel) parent.getSelectedItem();
                if (vehicle != null) {
                    setVehicletype(vehicle.getId());
                    if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                        UtilMethods.INSTANCE.model(context, vehicle.getId(), new mCallBackResponse() {
                            @Override
                            public void success(String from, String message) {
                                Type type = new TypeToken<List<BrandModel>>(){}.getType();
                                modelList = new Gson().fromJson(message, type);
                                modelAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, modelList);
                                spinModel.setPrompt("Select Vehicle Brand");
                                spinModel.setAdapter(
                                        new NothingSelectedSpinnerAdapter(
                                                modelAdapter,
                                                R.layout.contact_spinner_row_nothing_selected_model,
                                                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                                context));

                                llModel.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void fail(String from) {

                            }
                        });

                    } else {

                        UtilMethods.INSTANCE.internetNotAvailableMessage(context);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BrandModel vehicle = (BrandModel) parent.getSelectedItem();
                if (vehicle!=null) {
                    setVehiclemodel(vehicle.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinFuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinFuel.getSelectedItem()!=null) {
                    setFueltype(""+spinFuel.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setVehiclenumber(etVehicleNumber.getText().toString());
            }
        });

        etPurpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setVehiclenumber(etPurpose.getText().toString());
            }
        });

        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        // do something awesome with the selected place
                        setLatitude("");
                        setLongitude("");
                        setLocation(placesAutocomplete.getText().toString());
                        placesAutocomplete.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(PlaceDetails details) {
                                Log.d("test", "details " + details);
//                                mStreet.setText(details.name);
                                for (AddressComponent component : details.address_components) {
                                    for (AddressComponentType type : component.types) {
                                        switch (type) {
                                            case STREET_NUMBER:
                                                break;
                                            case ROUTE:
                                                break;
                                            case NEIGHBORHOOD:
                                                break;
                                            case SUBLOCALITY_LEVEL_1:
                                                break;
                                            case SUBLOCALITY:
                                                break;
                                            case LOCALITY:
                                                break;
                                            case ADMINISTRATIVE_AREA_LEVEL_1:
//                                                txtArea.setText(component.short_name);
                                                break;
                                            case ADMINISTRATIVE_AREA_LEVEL_2:
                                                break;
                                            case COUNTRY:
                                                break;
                                            case POSTAL_CODE:
                                                setPincode(component.long_name);
                                                break;
                                            case POLITICAL:
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }
        );
    }

    protected RequestDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public void setVehiclenumber(String vehiclenumber) {
        this.vehiclenumber = vehiclenumber;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getVehiclemodel() {
        return vehiclemodel;
    }

    public void setVehiclemodel(String vehiclemodel) {
        this.vehiclemodel = vehiclemodel;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
