package com.example.assessmentandroid;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assessmentandroid.filter.FilterItem;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder>  {
    private FilterItem[] mDataset;

    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView dateRange;
        public TextView gender;
        public TextView countries;
        public TextView color;

            public FilterViewHolder(LinearLayout v) {
            super(v);
            layout = v;
            dateRange = (TextView) layout.findViewById(R.id.text_dateRange);
            gender = (TextView) layout.findViewById(R.id.text_gender);
            countries = (TextView) layout.findViewById(R.id.text_coutry);
            color = (TextView) layout.findViewById(R.id.text_color);
        }
    }

    public FilterAdapter(FilterItem[] dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public FilterAdapter.FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent,false);

        FilterViewHolder vh = new FilterViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.FilterViewHolder holder, int position) {
        FilterItem item = mDataset[position];

        //holder.dateRange.setText("Date Range: " +  item.date); //missing data from the filter
        holder.gender.setText("Gender: " + item.gender);
        holder.countries.setText("Countries: " + convertStringArrayToString(item.countries));
        holder.color.setText("Color: " + convertStringArrayToString(item.colors));
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
