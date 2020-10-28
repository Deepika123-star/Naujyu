package com.smartwebarts.developer.naujyu.brands;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.models.BrandModel;
import com.smartwebarts.developer.naujyu.models.VehicleModel;
import com.smartwebarts.developer.naujyu.retrofit.UtilMethods;
import com.smartwebarts.developer.naujyu.retrofit.mCallBackResponse;
import com.smartwebarts.developer.naujyu.utils.Toolbar_Set;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModelActivity extends AppCompatActivity {

    public static final String BRAND_LIST = "data";
    public static final String VEHICLE_TYPE = "vehicle";
    private BrandModel brandModel;
    private Context context;
    private List<BrandModel> modelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ModelAdapter adapter;
    private VehicleModel vehicleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        context = this;
        Toolbar_Set.INSTANCE.setToolbar(this, "Select Model");
        Toolbar_Set.INSTANCE.setBottomNav(this);

        brandModel = (BrandModel) getIntent().getExtras().get(BRAND_LIST);
        vehicleModel = (VehicleModel) getIntent().getExtras().get(VEHICLE_TYPE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        adapter = new ModelAdapter(context, modelList, brandModel, vehicleModel);
        recyclerView.setAdapter(adapter);
        if (brandModel!=null) {
            getModelList();
        }
    }

    private void getModelList() {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

            UtilMethods.INSTANCE.model(context, brandModel.getId(), new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type type = new TypeToken<List<BrandModel>>(){}.getType();
                    modelList = new Gson().fromJson(message, type);
                    adapter.updateList(modelList);
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