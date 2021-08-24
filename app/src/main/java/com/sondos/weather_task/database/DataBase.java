package com.sondos.weather_task.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sondos.weather_task.model.City;

import java.util.ArrayList;
import java.util.List;

public class DataBase  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WeatherDatabase";
    private static final int VERSION = 1;

    // ------ CityTable ------
    private final String CITY_TABLE_NAME = "City";
    private final String CITY_NAME = "name";
    private final String CITY_TEMP_C = "tempC";
    private final String CITY_TEMP_F = "tempF";

    public DataBase(Context context) {
        super(context,DATABASE_NAME , null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createCityTable = "CREATE TABLE " + CITY_TABLE_NAME + "("
                +CITY_NAME + " TEXT PRIMARY KEY ,"
                +CITY_TEMP_C + " REAL,"
                +CITY_TEMP_F + " REAL) " ;
        sqLiteDatabase.execSQL(createCityTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addCity(City city){

        SQLiteDatabase db = this.getWritableDatabase();

//        String ROW1 = "INSERT INTO " + CITY_TABLE_NAME + " ("
//                + CITY_NAME + ", " + CITY_TEMP_C + ", "
//                + CITY_TEMP_F + ") Values ('"+city.getName().replace("'","")+"', "+city.getTempC()+" , "+city.getTempF()+")";
//        db.execSQL(ROW1);
//
        ContentValues values = new ContentValues();

        values.put(CITY_NAME,city.getName());
        values.put(CITY_TEMP_C,city.getTempC());
        values.put(CITY_TEMP_F,city.getTempF());

        db.insert(CITY_TABLE_NAME, null, values);

        db.close();
    }

    public List<City> getAllCities(){
        List<City> cities = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CITY_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                City city = new City(cursor.getString(0).toString(),
                        Float.parseFloat(cursor.getString(1).toString()),
                        Float.parseFloat(cursor.getString(2).toString()));
                cities.add(city);
            } while (cursor.moveToNext());
        }

        db.close();
        return cities;
    }

    public int updateCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CITY_NAME, city.getName());
        values.put(CITY_TEMP_C, city.getTempC());
        values.put(CITY_TEMP_F, city.getTempF());

        // updating row
        int updatedRows = db.update(CITY_TABLE_NAME, values, CITY_NAME + " = ?",
                new String[] { String.valueOf(city.getName()) });

        db.close();
        return updatedRows;
    }


    public City getByName(String cityName){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CITY_TABLE_NAME, new String[] { CITY_NAME }, CITY_NAME + "=?",
                new String[] { cityName }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        else
         return new City(cityName,0,0);

        City city = new City(cursor.getString(0).toString(),
                Float.parseFloat(cursor.getString(1).toString()),
                Float.parseFloat(cursor.getString(2).toString()));

        db.close();

        return city;
    }

    public void deleteCity(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CITY_TABLE_NAME, CITY_NAME + " = ?",
                new String[] { name });
        db.close();
    }


}
