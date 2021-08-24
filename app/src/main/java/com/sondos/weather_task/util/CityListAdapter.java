package com.sondos.weather_task.util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sondos.weather_task.R;
import com.sondos.weather_task.database.DataBase;
import com.sondos.weather_task.model.City;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityListAdapter extends BaseAdapter {

    private final Activity context;
    private List<City> cities ;
    DataBase db ;

    public CityListAdapter(Activity context, List<City> cities, DataBase db) {
        this.context = context;
        this.cities = cities;
        this.db = db;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final CityView holder;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.city_view, null, true);

        City city= (City) getItem(i);
        holder = new CityView();

        holder.name = view.findViewById(R.id.name);
        holder.c = view.findViewById(R.id.c);
        holder.f = view.findViewById(R.id.f);
        holder.icon = view.findViewById(R.id.icon);


        holder.name.setText(city.getName());
        holder.c.setText(city.getTempC()+" °C");
        holder.f.setText(city.getTempF()+" °F");

        if (city.getTempC()>= 32.5){
            holder.icon.setImageResource(R.drawable.sunny);
        }else {
            holder.icon.setImageResource(R.drawable.cloud);
        }

        return view;
    }


    private class CityView{
        protected ImageView icon;
        protected TextView name;
        protected TextView f;
        protected TextView c;

    }
}
