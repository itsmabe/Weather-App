package com.me.myweatherapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.me.myweatherapp.db.entity.City;

import java.util.List;

public class CityRepository {

    private String DB_NAME = "db_weather";

    private CityDatabase cityDatabase;

    public CityRepository(Context context) {
        cityDatabase = Room.databaseBuilder(context, CityDatabase.class, DB_NAME).build();
    }

    public void insertCity(String name) {
        City city = new City();
        city.setName(name);
        insertCity(city);
    }

    public void insertCity(final City city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cityDatabase.daoAccess().insertCity(city);
                return null;
            }
        }.execute();
    }

    public void deleteCity(final String name) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cityDatabase.daoAccess().deleteCity(name);
                return null;
            }
        }.execute();

    }
    public void deleteCity(final int id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                cityDatabase.daoAccess().deleteCity(id);
                return null;
            }
        }.execute();

    }

    public LiveData<City> getCity(int id) {
        return cityDatabase.daoAccess().getCity(id);
    }

    public LiveData<City> getCity(String name) {
        return cityDatabase.daoAccess().getCity(name);
    }

    public LiveData<List<City>> getCities() {
        return cityDatabase.daoAccess().fetchAllCities();
    }
}
