package com.smartwebarts.developer.naujyu;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.smartwebarts.developer.naujyu.adapter.CoursesAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CoursesAdapter adapter;
    public static final String TITLE_KEY = "title";
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        title = getIntent().getExtras().getString(TITLE_KEY, "");
toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //toolBarLayout.setTitle(title);

        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CoursesAdapter(this, null);
        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("Deepika ");
        list.add("Deepika");
        list.add("Abdullah");
        list.add("Neha");
        list.add("Neha");




        adapter.setList(list);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
}