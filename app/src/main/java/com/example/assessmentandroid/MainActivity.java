package com.example.assessmentandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assessmentandroid.filter.CarItem;
import com.example.assessmentandroid.filter.FilterItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;


public class MainActivity extends AppCompatActivity implements OnItemClick  {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FilterItem[] mDataset;
    private HashMap<Long, Map> cacheCSV;
    private ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set data
        cacheCSV = new HashMap();
        mDataset = getmDataset(R.raw.dummy_filter);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FilterAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);

        DataAsyncTasks dataAsyncTasks = new DataAsyncTasks();
        dataAsyncTasks.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(FilterItem value) {
        Log.d("Filter",value.toString());
        CarItem[] items = filterAndServe(value);
        Intent i = new Intent(this, CarActivity.class);
        i.putExtra("data", items);
        this.startActivity(i);
    }

    public class DataAsyncTasks extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params) {
            getAndCacheCSV();
            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Caching CSV, Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            FilterAsyncTasks filterAsyncTasks = new FilterAsyncTasks();
            filterAsyncTasks.execute();
        }
    }

    public class FilterAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            getFilterJson();
            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching filters, Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
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
                FilterAsyncTasks filterAsyncTasks = new FilterAsyncTasks();
                filterAsyncTasks.execute();
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
                long key = row.getOriginalLineNumber();
                if (key > 1) {
                    Map<String,String> rowObj = new HashMap();
                    rowObj.put("first_name",row.getField(1));
                    rowObj.put("last_name",row.getField(2));
                    rowObj.put("email",row.getField(3));
                    rowObj.put("country",row.getField(4));
                    rowObj.put("car_model",row.getField(5));
                    rowObj.put("car_model_year",row.getField(6));
                    rowObj.put("car_color",row.getField(7));
                    rowObj.put("gender",row.getField(8));
                    rowObj.put("job_title",row.getField(9));
                    rowObj.put("bio",row.getField(10));

                    cacheCSV.put(key,rowObj);

                }
            }

            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private CarItem[] filterAndServe(FilterItem item){
        CarItem[] cars = null;
        try {
            Map<Object, Object> items1 = new HashMap();
            Map<Object, Object> items2 = new HashMap();
            List<String> searchColor = new ArrayList<String>();
            List<String> searchCountry = new ArrayList<String>();
            searchCountry.addAll(Arrays.asList(item.countries));
            searchColor.addAll(Arrays.asList(item.colors));

            for (String country : searchCountry) {
                Map<Object, Object> filtered = cacheCSV.entrySet()
                        .stream()
                        .filter(map -> map.getValue().containsValue(country))
                        .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
                items1.putAll(filtered);
            }
            if (searchColor.size() > 0){
                for (String color : searchColor) {
                    Map<Object, Object> filtered = items1.entrySet()
                            .stream()
                            .filter(map -> {
                                HashMap obj = (HashMap) map.getValue();
                                return obj.containsValue(color) ? true : false;
                            })
                            .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
                    items2.putAll(filtered);
                }
            }
            else items2.putAll(items1);

            int i = 0;
            cars = new CarItem[items2.size()];
            for (Map.Entry<Object, Object> entry : items2.entrySet()) {
                HashMap<String,String> values = (HashMap<String, String>) entry.getValue();
                String[] key = values.keySet().toArray(new String[0]);
                String[] value = values.values().toArray(new String[0]);
                cars[i] = new CarItem();
                for (Field f : CarItem.class.getFields()){
                    if(values.containsKey(f.getName())){
                        f.set(cars[i],values.get(f.getName()));
                    }
                }
                i++;
            }
        }
        catch (Exception e){
            Log.e("ERROR ","Filter Error: ",e);
            cars = new CarItem[0];
        }
        finally {
            return cars;
        }
    }
}