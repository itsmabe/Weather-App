package com.me.myweatherapp.view.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.me.myweatherapp.R;
import com.me.myweatherapp.db.CityRepository;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            CityRepository repository = new CityRepository(this);
            repository.insertCity("Casablanca");
            repository.insertCity("Fez");
            repository.insertCity("Marrakech");
            repository.insertCity("Tangier");
            repository.insertCity("Rabat");
            prefs.edit().putBoolean("firstrun", false).commit();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WeatherFragment.newInstance())
                    .commitNow();
        }
    }
}
