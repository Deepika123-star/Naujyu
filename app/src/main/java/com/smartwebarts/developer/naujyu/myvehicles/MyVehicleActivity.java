package com.smartwebarts.developer.naujyu.myvehicles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.smartwebarts.developer.naujyu.GPSTracker;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.ServicesActivity;
import com.smartwebarts.developer.naujyu.brands.BrandActivity;
import com.smartwebarts.developer.naujyu.database.DatabaseClient;
import com.smartwebarts.developer.naujyu.database.Task;
import com.smartwebarts.developer.naujyu.utils.LocationAddress;
import com.smartwebarts.developer.naujyu.utils.Toolbar_Set;

import java.util.ArrayList;
import java.util.List;

public class MyVehicleActivity extends AppCompatActivity {

    Context context;
    RecyclerView recyclerView;
    MyActivityAdapter adapter;
    private GPSTracker gpsTracker;
    private LocationAddress locationAddress;
    private double mLat, mLang;

    List<Task> tasks = new ArrayList<>();
    private String strlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicle);

        context = this;
        Toolbar_Set.INSTANCE.setToolbar(this, "Request Service");
        recyclerView = findViewById(R.id.recyclerView);
        gpsTracker = new GPSTracker(this);
        locationAddress = new LocationAddress();
        gpsTracker.getLocation();
        adapter = new MyActivityAdapter(context, tasks);

        recyclerView.setAdapter(adapter);
        if (gpsTracker.canGetLocation()){

            mLat = gpsTracker.getLatitude();
            mLang = gpsTracker.getLongitude();

            locationAddress.getAddressFromLocation(mLat, mLang,
                    getApplicationContext(), new GeocoderHandler());

        } else {
            gpsTracker.showSettingsAlert();
        }
        getCarList();
//        Toolbar_Set.INSTANCE.setBottomNav(this);
    }

    public void openBrandActivity(View view) {
        startActivity(new Intent(context, BrandActivity.class));
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

            adapter.setLocation(strlocation);
            adapter.setLatitude(""+mLat);
            adapter.setLongitude(""+mLang);
            adapter.setPincode("");
        }
    }

    private void getCarList() {
        class GetTasks extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {
                List<Task> tasks= DatabaseClient.getmInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return new ArrayList<>(tasks);
            }

            @Override
            protected void onPostExecute(ArrayList<Task> tasks) {
                adapter.updateList(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(this, ServicesActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}