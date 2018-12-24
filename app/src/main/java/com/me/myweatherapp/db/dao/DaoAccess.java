package com.me.myweatherapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.me.myweatherapp.db.entity.City;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    Long insertCity(City city);


    @Query("SELECT * FROM City")
    LiveData<List<City>> fetchAllCities();

    @Query("SELECT * FROM City WHERE id=:cityId")
    LiveData<City> getCity(int cityId);

    @Query("SELECT * FROM City WHERE name=:cityName")
    LiveData<City> getCity(String cityName);


    @Query("DELETE FROM City WHERE name=:cityName")
    void deleteCity(String cityName);

    @Query("DELETE FROM City WHERE id=:cityId")
    void deleteCity(int cityId);
}
