package com.example.greenlifeuser.interfaces;

import com.example.greenlifeuser.weatherapi.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("weather")
    Call<WeatherResponse> getWeatherData(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}

