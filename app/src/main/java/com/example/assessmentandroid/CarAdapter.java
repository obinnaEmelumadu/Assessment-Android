package com.example.assessmentandroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assessmentandroid.filter.CarItem;
import com.example.assessmentandroid.filter.FilterItem;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder>  {
    private CarItem[] mDataset;
    private Context mcon;

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView name;
        public TextView email;
        public TextView country;
        public TextView car;
        public TextView gender;
        public TextView job;
        public TextView bio;

        public CarViewHolder(LinearLayout v) {
            super(v);
            layout = v;
            name = layout.findViewById(R.id.name);
            email = layout.findViewById(R.id.email);
            country = layout.findViewById(R.id.country);
            car = layout.findViewById(R.id.car);
            gender = layout.findViewById(R.id.gender);
            job = layout.findViewById(R.id.job);
            bio = layout.findViewById(R.id.bio);
        }
    }

    public CarAdapter(CarItem[] dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcon = parent.getContext();
        LinearLayout v = (LinearLayout) LayoutInflater.from(mcon).inflate(R.layout.cars_item, parent,false);

        CarAdapter.CarViewHolder vh = new CarAdapter.CarViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.CarViewHolder holder, final int position) {
        CarItem item = mDataset[position];

        holder.name.setText("Full NAme: " +  item.first_name + " " +item.last_name);
        holder.email.setText("Email: " +  item.email);
        holder.country.setText("Country: " +  item.country);
        holder.car.setText("Car: " +  item.car_model);
        holder.gender.setText("Gender: " +  item.gender);
        holder.job.setText("Job: " +  item.job_title);
        holder.bio.setText("Bio: " +  item.bio);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    private static String convertStringArrayToString(String[] strArr) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(",");
        return sb.toString();
    }
}
