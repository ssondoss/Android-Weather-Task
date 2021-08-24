package com.sondos.weather_task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.sondos.weather_task.database.DataBase;
import com.sondos.weather_task.model.City;
import com.sondos.weather_task.util.CityListAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView cities;

    private OkHttpClient client = new OkHttpClient();

    private List<City> cityList = new ArrayList<>();

    public static int COUNT = 0;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cities = findViewById(R.id.cities);
        db=new DataBase(MainActivity.this);
        for (String city:getResources().getStringArray(R.array.cities)) {

            OkHttpHandler okHttpHandler= new OkHttpHandler();
            okHttpHandler.execute(getResources().getString(R.string.api) + city);

        }

    }

    public class OkHttpHandler extends AsyncTask {
        OkHttpClient client = new OkHttpClient();
        @Override
        protected Object doInBackground(Object[] objects) {

            if(COUNT<12){
            Request.Builder builder = new Request.Builder();
            builder.url(objects[0].toString());
            Request request = builder.build();
            COUNT++;
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
                City city= new City();
                String[] split = objects[0].toString().split("q=");
                City byName = db.getByName(split[split.length - 1]);
                db.deleteCity(split[split.length - 1]);
                db.addCity(byName);
                checkIfListIsReady();
            }}
            return null;
        }

        @Override
        protected void onPostExecute(Object s) {
            if(COUNT<12){
            JSONObject jsonObject = objectToJSONObject(s);
            super.onPostExecute(s);
            City city= new City();

            try {

                city.setName(jsonObject.getJSONObject("location").getString("name"));
                city.setTempC(Float.parseFloat(jsonObject.getJSONObject("current").getString("temp_c")));
                city.setTempF(Float.parseFloat(jsonObject.getJSONObject("current").getString("temp_f")));

                db.deleteCity(jsonObject.getJSONObject("location").getString("name"));
                db.addCity(city);

                cityList.add(city);
                checkIfListIsReady();

            } catch (JSONException e) {
                e.printStackTrace();
            }}
        }

        public void checkIfListIsReady(){
            if(COUNT == 12){
                final CityListAdapter adapter=new CityListAdapter(MainActivity.this, cityList,db);
                cities.setAdapter(adapter);
            }
        }
    }

    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }


}