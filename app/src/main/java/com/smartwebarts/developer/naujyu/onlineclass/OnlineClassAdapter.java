package com.smartwebarts.developer.naujyu.onlineclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.developer.naujyu.R;

import java.util.List;

public class OnlineClassAdapter extends RecyclerView.Adapter<OnlineClassAdapter.MYViewHolder> {

    Context context;
    List<String> list;

    public OnlineClassAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.youtube_item_layout,parent,false);
        return new MYViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MYViewHolder holder, int position) {
        holder.textView.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MYViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);

        }

    }
}
