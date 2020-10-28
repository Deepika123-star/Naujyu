package com.smartwebarts.developer.naujyu.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class SaveWishList extends AsyncTask<String, String, String> {

    private List<WishItem> Tasks;
    private Context context;

    public SaveWishList(Context context, List<WishItem> Tasks) {
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
                .wishDao()
                .insert(Tasks);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
