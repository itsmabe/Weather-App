package com.me.myweatherapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.me.myweatherapp.db.entity.City;
import com.me.myweatherapp.db.dao.DaoAccess;

@Database(entities = {City.class}, version = 1, exportSchema = false)
public abstract class CityDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}