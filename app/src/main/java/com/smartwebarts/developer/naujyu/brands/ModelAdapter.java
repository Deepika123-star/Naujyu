package com.smartwebarts.developer.naujyu.brands;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.database.SaveProductList;
import com.smartwebarts.developer.naujyu.database.Task;
import com.smartwebarts.developer.naujyu.dialog.NothingSelectedSpinnerAdapter;
import com.smartwebarts.developer.naujyu.models.BrandModel;
import com.smartwebarts.developer.naujyu.models.VehicleModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.MyViewHolder> {

    private Context context;
    private List<BrandModel> modelList;
    private BrandModel brandModel;
    final Calendar myCalendar = Calendar.getInstance();
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private VehicleModel vehicleModel;

    public ModelAdapter(Context context, List<BrandModel> modelList, BrandModel brandModel, VehicleModel vehicleModel) {
        this.context = context;
        this.modelList = modelList;
        this.brandModel = brandModel;
        this.vehicleModel = vehicleModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_view2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.categoryName.setText(modelList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(position, modelList.get(position));
            }
        });
    }

    private void openDialog(int position, BrandModel model) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_request_n, null);
        dialog.setTitle("Save vehicle");
        TextInputEditText etVehicleNumber = dialogview.findViewById(R.id.etVehicleNumber);
        TextInputEditText etYear = dialogview.findViewById(R.id.dob);
        Spinner spinFuel = dialogview.findViewById(R.id.fueltype);

        ArrayAdapter fuelAdapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, context.getResources().getStringArray(R.array.fueltypes));
        spinFuel.setPrompt("Select Fuel");
        spinFuel.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        fuelAdapter,
                        R.layout.contact_spinner_row_nothing_selected_fuel,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        context));

        etYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(etYear);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etVehicleNumber.getText().toString().length()!=10 && !etVehicleNumber.getText().toString().matches("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$")){
                    return;
                }
                if (etYear.getText().toString().isEmpty()) {
                    return;
                }

                String fuelType = spinFuel.getSelectedItem().toString();

                if (fuelType.isEmpty()) {
                    Toast.makeText(context, "Select fuel type of your car", Toast.LENGTH_SHORT).show();
                    return;
                }
                save(position, model, etYear.getText().toString(), etVehicleNumber.getText().toString(),fuelType);
            }
        });


        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });


        dialog.setView(dialogview);
        dialog.show();
    }

    private void save(int position, BrandModel model, String year, String vehiclenumber, String fuelType) {
        Task task = new Task(
                vehicleModel.getId(),
                brandModel.getId(),
                brandModel.getName(),
                model.getId(),
                model.getName(),
                vehicleModel.getName(),
                year,
                vehiclenumber,
                "",
                fuelType);

        List<Task> items = new ArrayList<>();
        items.add(task);
        new SaveProductList(context,items).execute();
    }

    private void selectDate(TextInputEditText etYear) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.date_picker);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final DatePicker datePicker = dialog.findViewById(R.id.date_picker);
        Button done = dialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                myCalendar.set(year, month, day);

                etYear.setText(sdf.format(myCalendar.getTime()));
                Log.e("dob", sdf.format(myCalendar.getTime()));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void updateList(List<BrandModel> brandList) {
        this.modelList = brandList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
        }
    }
}
