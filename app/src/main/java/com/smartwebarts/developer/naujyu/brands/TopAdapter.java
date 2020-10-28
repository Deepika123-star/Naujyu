package com.smartwebarts.developer.naujyu.brands;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.models.BrandModel;
import com.smartwebarts.developer.naujyu.models.VehicleModel;
import com.smartwebarts.developer.naujyu.utils.ApplicationConstants;
import com.smartwebarts.developer.naujyu.utils.MyGlide;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.MyViewHolder> {

    private Context context;
    private List<BrandModel> brandList;
    private VehicleModel vehicleModel;

    public TopAdapter(Context context, List<BrandModel> brandList) {
        this.context = context;
        this.brandList = brandList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String url = "";
        try {
            url = ApplicationConstants.INSTANCE.BRAND_IMAGES+brandList.get(position).getName().toLowerCase().replaceAll("\\s+", "").trim() +".png";
        } catch (Exception ignored) {}
        MyGlide.with(context, url, holder.imageView);

        holder.categoryName.setText(brandList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModelActivity.class);
                intent.putExtra(ModelActivity.BRAND_LIST, brandList.get(position));
                intent.putExtra(ModelActivity.VEHICLE_TYPE, vehicleModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public void updateList(List<BrandModel> brandList) {
        this.brandList = brandList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView categoryName;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.tvName);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
        }
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
}
