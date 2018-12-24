package com.me.myweatherapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.me.myweatherapp.db.entity.City;
import com.me.myweatherapp.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewModel extends ViewModel {

    private static final String WEATHER_URL = "https://api.worldweatheronline.com/premium/v1/weather.ashx?key=0e68353c616c4684a5f173341182112&num_of_days=1&format=json&q=";

    private MutableLiveData<List<Weather>> citiesWeatherData;
    private MutableLiveData<Weather> cityWeatherData;

    public LiveData<List<Weather>> getAllCitiesWeather(Context ctx, List<City> cities) {
        citiesWeatherData = new MutableLiveData();
        StringBuilder queryBuilder = new StringBuilder();
        for (City city : cities) {
            queryBuilder.append(city.getName());
            queryBuilder.append(";");
        }

        loadData(ctx, queryBuilder.toString());
        return citiesWeatherData;
    }

    public LiveData<Weather> getCityWeather(Context ctx, String query) {
        cityWeatherData = new MutableLiveData();
        loadData(ctx, query);
        return cityWeatherData;
    }


    private void loadData(Context ctx, final String query) {
        final List<Weather> data = new ArrayList();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, WEATHER_URL + query, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonData = response.getJSONObject("data");
                            if (jsonData.has("area")) {
                                JSONArray area = jsonData.getJSONArray("area");
                                for (int i = 0; i < area.length(); i++) {
                                    JSONArray condition = area.getJSONObject(i).getJSONArray("current_condition");
                                    JSONArray request = area.getJSONObject(i).getJSONArray("request");
                                    String query = request.getJSONObject(0).getString("query");
                                    String date = condition.getJSONObject(0).getString("observation_time");
                                    String temperature = condition.getJSONObject(0).getString("temp_C");
                                    String state = condition.getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
                                    data.add(new Weather(query.split(",")[0], query, date, temperature + "°C", state));
                                }
                                citiesWeatherData.setValue(data);
                            } else {
                                JSONArray condition = jsonData.getJSONArray("current_condition");
                                JSONArray request = jsonData.getJSONArray("request");
                                String query = request.getJSONObject(0).getString("query");
                                String date = condition.getJSONObject(0).getString("observation_time");
                                String temperature = condition.getJSONObject(0).getString("temp_C");
                                String state = condition.getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
                                cityWeatherData.setValue(new Weather(query.split(",")[0], query, date, temperature + "°C", state));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(jsonObjectRequest);
    }
}
