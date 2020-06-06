package com.example.assessmentandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assessmentandroid.filter.CarItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import de.siegmar.fastcsv.writer.CsvAppender;
import de.siegmar.fastcsv.writer.CsvWriter;

public class CarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CarItem[] mDataset;
    private ProgressDialog progressDialog;

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
        mDataset = (CarItem[]) getIntent().getSerializableExtra("data");
        recyclerView = findViewById(R.id.recycler_list);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CarAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                CarActivity.SaveDataAsyncTasks saveDataAsyncTasks = new CarActivity.SaveDataAsyncTasks();
                saveDataAsyncTasks.execute();
                break;
        }
        return true;
    }

    public class SaveDataAsyncTasks extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params) {
            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Decagon");
            boolean isPresent = true;
            if (!docsFolder.exists()) {
                isPresent = docsFolder.mkdir();
            }

            File file = null;
            try {
                if (isPresent) {
                    file = new File(docsFolder.getAbsolutePath(), "file.csv");
                }
                else{
                    throw new  IOException();
                }
            } catch (IOException e) {
                e.printStackTrace();
                file = new File( "file.csv");
            }

            CsvWriter csvWriter = new CsvWriter();

            try (CsvAppender csvAppender = csvWriter.append(file, StandardCharsets.UTF_8)) {
                // header
                csvAppender.appendLine("id",
                        "first_name", "last_name", "email",
                        "country", "car_model", "car_model_year",
                        "car_color", "gender", "job_title", "bio");

                // 1st line in one operation
                int i = 1;
                for (CarItem car : mDataset) {
                    csvAppender.appendLine(String.valueOf(i),
                            car.first_name, car.last_name, car.email,
                            car.country, car.car_model, car.car_model_year,
                            car.car_color, car.gender, car.job_title, car.bio);
                    i++;
                }
                csvAppender.endLine();

                Toast.makeText(getBaseContext(), "File saved successfully!",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();


            }
            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CarActivity.this);
            progressDialog.setMessage("Saving CSV, Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
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