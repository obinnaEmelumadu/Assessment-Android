package com.example.assessmentandroid;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.assessmentandroid.filter.FilterItem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FilterItem[] mDataset;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set data
        mDataset = getmDataset(R.raw.dummy_filter);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FilterAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
    }

    private FilterItem[] getmDataset(@IdRes int id){
        try {
            Gson gson = new Gson();
            @SuppressLint("ResourceType") String jj = jsonStr(id);
            return gson.fromJson(jj, FilterItem[].class);
        }
        catch (Exception e){
            Log.e("Exception", String.valueOf(e));
            return new FilterItem[0];
        }
    }

    private String jsonStr(@IdRes int id) throws IOException {
        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        return writer.toString();
    }
}