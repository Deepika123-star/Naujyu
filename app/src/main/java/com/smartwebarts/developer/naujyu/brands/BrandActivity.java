package com.smartwebarts.developer.naujyu.brands;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.dialog.NothingSelectedSpinnerAdapter;
import com.smartwebarts.developer.naujyu.models.BrandModel;
import com.smartwebarts.developer.naujyu.models.VehicleModel;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.utils.Toolbar_Set;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BrandActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Context context;
    private ArrayAdapter vehicleAdapter, brandAdapter;
    private List<VehicleModel> vehicleList;
    private Spinner spinVehicle;
    private List<BrandModel> brandList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        context = this;
        Toolbar_Set.INSTANCE.setToolbar(this, "Select brand");
        Toolbar_Set.INSTANCE.setBottomNav(this);

        spinVehicle = findViewById(R.id.vehicletype);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new TopAdapter(context, brandList);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.setAdapter(adapter);
        getVehicleList();

    }

    private void setVehicleTytpeSpinner() {
        vehicleAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, vehicleList);
        spinVehicle.setPrompt("Select Vehicle Type");
        spinVehicle.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        vehicleAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        context));

        spinVehicle.setOnItemSelectedListener(this);
    }

    private void getVehicleList() {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            UtilMethods.INSTANCE.vehicletype(this, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    if (!message.isEmpty()) {
                        Type listType = new TypeToken<ArrayList<VehicleModel>>(){}.getType();
                        vehicleList = new Gson().fromJson(message, listType);
                        setVehicleTytpeSpinner();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        VehicleModel vehicle = (VehicleModel) parent.getSelectedItem();
        if (vehicle != null) {

            adapter.setVehicleModel(vehicle);

            if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                UtilMethods.INSTANCE.brand(context, vehicle.getId(), new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {
                        Type type = new TypeToken<List<BrandModel>>() {}.getType();
                        brandList = new Gson().fromJson(message, type);
                        setTopRecycler();
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

    private void setTopRecycler() {
        adapter.updateList(brandList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}