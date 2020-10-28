package com.smartwebarts.developer.naujyu.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.smartwebarts.developer.naujyu.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceList {

    private String name;
    private Drawable drawable;

    public ServiceList(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public static List<ServiceList> getServiceList(Context context) {
        List<ServiceList> list = new ArrayList<>();

        list.add(new ServiceList("Courses",
                context.getDrawable(R.drawable.courses)));

        list.add(new ServiceList("City",
                context.getDrawable(R.drawable.city)));

        list.add(new ServiceList("result",
                context.getDrawable(R.drawable.result)));

        list.add(new ServiceList("Collage",
                context.getDrawable(R.drawable.college)));

        list.add(new ServiceList("Exam",
                context.getDrawable(R.drawable.exam)));

        list.add(new ServiceList("Chat",
                context.getDrawable(R.drawable.chat)));


        list.add(new ServiceList("Best Book For Exam",
                context.getDrawable(R.drawable.book)));

        list.add(new ServiceList("Coaching Center",
                context.getDrawable(R.drawable.coaching)));

        list.add(new ServiceList("Prev. Exam Papers",
                context.getDrawable(R.drawable.exampapers)));

        list.add(new ServiceList("Compare Collage",
                context.getDrawable(R.drawable.compare1)));

        list.add(new ServiceList("Compare Coachings",
                context.getDrawable(R.drawable.compare2)));

        list.add(new ServiceList("School Partners",
                context.getDrawable(R.drawable.collageparternership)));

        list.add(new ServiceList("Online Classes",
                context.getDrawable(R.drawable.onlineclasses)));

        list.add(new ServiceList("Test Series",
                context.getDrawable(R.drawable.testseries)));


        list.add(new ServiceList("Study Material",
                context.getDrawable(R.drawable.study)));


        list.add(new ServiceList("Wallet",
                context.getDrawable(R.drawable.transfer_money)));











        return list;
    }

    public String getName() {
        return name;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
