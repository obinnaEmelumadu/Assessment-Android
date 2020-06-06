package com.example.assessmentandroid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assessmentandroid.filter.FilterItem;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder>  {
    private FilterItem[] mDataset;
    private OnItemClick mCallback;

    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView name;
        public TextView dateRange;
        public TextView gender;
        public TextView countries;
        public TextView color;

            public FilterViewHolder(LinearLayout v) {
            super(v);
            layout = v;
            name = layout.findViewById(R.id.text_name);
            dateRange = layout.findViewById(R.id.text_dateRange);
            gender = layout.findViewById(R.id.text_gender);
            countries = layout.findViewById(R.id.text_coutry);
            color = layout.findViewById(R.id.text_color);
        }
    }

    public FilterAdapter(FilterItem[] dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public FilterAdapter.FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent,false);
        mCallback = (OnItemClick) parent.getContext();
        FilterViewHolder vh = new FilterViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.FilterViewHolder holder, final int position) {
        final FilterItem item = mDataset[position];

        holder.name.setText("Created: " +  item.fullName);
        holder.dateRange.setText("Created: " +  item.createdAt);
        holder.gender.setText("Gender: " + item.gender);
        holder.countries.setText("Countries: " + convertStringArrayToString(item.countries));
        holder.color.setText("Color: " + convertStringArrayToString(item.colors));

        holder.layout.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick(item);
            }
        });
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
