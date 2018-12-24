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
import com.me.myweatherapp.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsViewModel extends ViewModel {

    private static final String WEATHER_URL = "https://api.worldweatheronline.com/premium/v1/weather.ashx?key=0e68353c616c4684a5f173341182112&num_of_days=1&format=json&q=";

    private MutableLiveData<Weather> weather;

    public LiveData<Weather> getWeather(Context ctx, String city) {
        weather = new MutableLiveData();
        loadData(ctx, city);
        return weather;
    }

    private void loadData(Context ctx, final String query) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, WEATHER_URL + query, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray condition = data.getJSONArray("current_condition");
                            String temperature = condition.getJSONObject(0).getString("temp_C");
                            String humidity = condition.getJSONObject(0).getString("humidity");
                            String wind = condition.getJSONObject(0).getString("windspeedKmph");
                            String pressure = condition.getJSONObject(0).getString("pressure");
                            weather.setValue(new Weather(temperature + "°C", "Humidity " + humidity + "%", "Wind Speed " + wind + " Km/h", "Pressure " + pressure + " hPa"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });


        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(jsonObjectRequest);
    }
}
