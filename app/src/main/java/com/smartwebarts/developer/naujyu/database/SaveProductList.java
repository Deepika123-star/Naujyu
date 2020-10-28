package com.smartwebarts.developer.naujyu.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.smartwebarts.developer.naujyu.myvehicles.MyVehicleActivity;

import java.util.List;

public class SaveProductList extends AsyncTask<String, String, String> {

    private List<Task> Tasks;
    private Context context;

    public SaveProductList(Context context, List<Task> Tasks) {
        this.Tasks = Tasks;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        DatabaseClient.getmInstance(context)
                .getAppDatabase()
                .taskDao()
                .insert(Tasks);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Intent intent = new Intent(context, MyVehicleActivity.class);
        context.startActivity(intent);
        ((Activity) context).finishAffinity();
    }
}
