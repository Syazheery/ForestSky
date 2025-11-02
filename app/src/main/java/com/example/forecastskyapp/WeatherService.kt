package com.example.forecastskyapp.api

import com.example.forecastskyapp.model.OneCallResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    fun getOneCallWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,alerts"
    ): Call<OneCallResponse>
}
