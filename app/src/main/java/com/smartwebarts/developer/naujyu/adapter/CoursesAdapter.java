package com.smartwebarts.developer.naujyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.developer.naujyu.R;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.MyViewHolder>{

    Context context;
    List<String> list;

    public CoursesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.courses_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }
    }
}
