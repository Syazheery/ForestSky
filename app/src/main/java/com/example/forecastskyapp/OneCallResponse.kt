package com.example.forecastskyapp.model

data class OneCallResponse(
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>
)

data class Current(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val visibility: Int,
    val wind_speed: Double,
    val weather: List<Weather>
)

data class Hourly(
    val dt: Long,
    val temp: Double,
    val weather: List<Weather>
)

data class Daily(
    val dt: Long,
    val temp: Temp,
    val weather: List<Weather>
)

data class Temp(
    val min: Double,
    val max: Double
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)
