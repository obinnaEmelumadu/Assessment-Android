package com.example.assessmentandroid;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assessmentandroid.filter.FilterItem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FilterItem[] mDataset;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set data
        mDataset = getmDataset(R.raw.dummy_filter);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FilterAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
        getAndCacheCSV();
        getFilterJson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                getFilterJson();
                break;
        }
        return true;
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

    private void getFilterJson(){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://android-json-test-api.herokuapp.com/accounts";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            mDataset =  gson.fromJson(response, FilterItem[].class);
                            mAdapter = new FilterAdapter(mDataset);
                            mAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(mAdapter);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley error", String.valueOf(error));
                }
            });
            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getAndCacheCSV(){
        InputStream inputStream = getResources().openRawResource(R.raw.car_ownsers_data);
        Reader inputStreamReader = new InputStreamReader(inputStream);
        CsvReader csvReader = new CsvReader();

        try (CsvParser csvParser = csvReader.parse(inputStreamReader) ){
            CsvRow row;
            while ((row = csvParser.nextRow()) != null) {
                if (row.getOriginalLineNumber() > 1) {
                    System.out.println("Read line: " + row);
                    System.out.println("First column of line: " + row.getField(0));
                }
            }

            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}