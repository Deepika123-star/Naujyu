package com.smartwebarts.developer.naujyu.myvehicles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.smartwebarts.developer.naujyu.LoginActivity;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.database.Task;
import com.smartwebarts.developer.naujyu.retrofit.BookingResponse;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.sharedpreferences.AppSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyActivityAdapter extends RecyclerView.Adapter<MyActivityAdapter.MyViewHolder> {

    private Context context;
    private List<Task> tasks;
    private String latitude, longitude, location, pincode;
    private String apiKey;
    private PlacesClient placesClient;

    public MyActivityAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.mycarsitems, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task model = tasks.get(position);
        if (model!=null){
            holder.title.setText(model.getModelname()+", "+model.getBrandname());
            holder.description.setText(model.getTypename()+" ("+model.getNumber()+")");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startBooking(model);
                }
            });
        }
    }

    private void startBooking(Task model) {
        AppSharedPreferences preferences = new AppSharedPreferences(((MyVehicleActivity)context).getApplication());
        if (!preferences.getLoginUserLoginId().isEmpty()){
            openSubmitDialog(model, preferences);
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("You are not logged in")
                    .setContentText("Login to continue")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            context.startActivity(new Intent(context, LoginActivity.class));
//                            ((MyVehicleActivity) context).finishAffinity();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private void openSubmitDialog(Task model, AppSharedPreferences preferences) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_booking, null);
        Spinner spinReasons = dialogview.findViewById(R.id.spinReasons);
        EditText etReasons = dialogview.findViewById(R.id.etReasons);
        PlacesAutocompleteTextView placesAutocomplete;
        apiKey = context.getString(R.string.location_api_key_id);

        if (!Places.isInitialized()) {
            Places.initialize(context.getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(context);
        placesAutocomplete = dialogview.findViewById(R.id.places_autocomplete);
        placesAutocomplete.setText(getLocation());
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
        spinReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    etReasons.setText("");
                } else {
                    etReasons.setText(spinReasons.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setView(dialogview);
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String reason = spinReasons.getSelectedItem().toString();
                if (reason.equalsIgnoreCase(context.getResources().getStringArray(R.array.reasonsforcancellation)[0])) {
                    reason = etReasons.getText().toString();
                    if (reason.isEmpty()) {
                        etReasons.setError("Enter a reason for service");
                    } else if (location.isEmpty()) {
                        placesAutocomplete.setError("Enter a location");
                    } else {
                        book(model, reason);
                    }
                } else {
                    book(model, spinReasons.getSelectedItem().toString());
                }

                dialog.dismiss();
            }
        });
    }

    private void book(Task model, String reason) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

            AppSharedPreferences preferences = new AppSharedPreferences(((MyVehicleActivity) context).getApplication());
            String customerId = preferences.getLoginUserLoginId();
            if (customerId.isEmpty()){
                Toast.makeText(context, "You are not logged in", Toast.LENGTH_SHORT).show();
                return;
            }
            String vehicleNumber = model.getNumber().toString();
            String vehicleType = model.getTypeid();
            String vehicleBrand = model.getBrandid();
            String vehicleModel = model.getModelid();
            String fuelType = "";
            String issue = reason;
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

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateList(ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
