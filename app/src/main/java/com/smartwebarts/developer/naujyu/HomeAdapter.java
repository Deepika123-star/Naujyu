package com.smartwebarts.developer.naujyu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.developer.naujyu.models.ServiceList;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context context;
    private List<ServiceList> serviceList;

    public HomeAdapter(Context context, List<ServiceList> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.home_services_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.text.setText(serviceList.get(position).getName());
        holder.image.setImageDrawable(serviceList.get(position).getDrawable());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    getCarList(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
        }
    }

    private void getCarList(int position) {

        switch (position) {
            case 0 : {
                Intent intent = new Intent(context, CoursesActivity.class);
                intent.putExtra(CoursesActivity.TITLE_KEY, serviceList.get(position).getName());
                context.startActivity(intent);
                break;
            }
            case 12:{
                Intent intent=new Intent(context,ProfileActivity.class);
                context.startActivity(intent);
                break;
            }





        }
    }
}
