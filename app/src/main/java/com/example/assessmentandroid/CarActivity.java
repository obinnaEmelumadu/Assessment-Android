package com.example.assessmentandroid;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.assessmentandroid.filter.CarItem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class CarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CarItem[] mDataset;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set data
        String data = (String) getIntent().getSerializableExtra("data");

        mDataset = getmDataset(data);
        recyclerView = findViewById(R.id.recycler_list);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CarAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
    }

    private CarItem[] getmDataset(String data){
        try {
            Gson gson = new Gson();
            return gson.fromJson(data, CarItem[].class);
        }
        catch (Exception e){
            Log.e("Exception", String.valueOf(e));
            return new CarItem[0];
        }
    }
}