package com.smartwebarts.developer.naujyu.onlineclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.smartwebarts.developer.naujyu.R;

import java.util.ArrayList;
import java.util.List;

public class OnlineclassActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlineclass);
        recyclerView=findViewById(R.id.onLineRecycler);
        List<OnlineClassModel>onlineClassModels=new ArrayList<>();
        onlineClassModels.add(new OnlineClassModel("Deepika"));
        onlineClassModels.add(new OnlineClassModel("Deepika"));
        onlineClassModels.add(new OnlineClassModel("Deepika"));
        onlineClassModels.add(new OnlineClassModel("Deepika"));
        onlineClassModels.add(new OnlineClassModel("Deepika"));
        onlineClassModels.add(new OnlineClassModel("Deepika"));

    }
}