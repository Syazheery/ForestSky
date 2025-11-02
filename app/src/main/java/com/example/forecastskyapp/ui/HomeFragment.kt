package com.example.forecastskyapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.forecastskyapp.R
import com.example.forecastskyapp.api.WeatherService
import com.example.forecastskyapp.model.OneCallResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    // --- Location (Selangor, Malaysia) ---
    private val lat = 3.0738
    private val lon = 101.5183
    private val apiKey = "2536eb8cd9877e40e1210b88dc689fee"

    // --- UI Components ---
    private lateinit var tvLocation: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCondition: TextView
    private lateinit var tvHighLow: TextView
    private lateinit var tvFeelsLike: TextView
    private lateinit var ivWeatherIcon: ImageView

    private lateinit var hourlyContainer: LinearLayout
    private lateinit var dailyContainer: LinearLayout

    private lateinit var humidityCard: TextView
    private lateinit var windCard: TextView
    private lateinit var visibilityCard: TextView
    private lateinit var pressureCard: TextView

    private lateinit var navToAccountButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout (use your existing activity_main.xml as the fragment layout)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        tvLocation = view.findViewById(R.id.tv_location)
        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvCondition = view.findViewById(R.id.tv_condition)
        tvHighLow = view.findViewById(R.id.tv_high_low)
        tvFeelsLike = view.findViewById(R.id.tv_feels_like)
        ivWeatherIcon = view.findViewById(R.id.iv_weather_icon)
        hourlyContainer = view.findViewById(R.id.hourly_container)
        dailyContainer = view.findViewById(R.id.daily_container)

        humidityCard = view.findViewById(R.id.card_humidity)
        windCard = view.findViewById(R.id.card_wind)
        visibilityCard = view.findViewById(R.id.card_visibility)
        pressureCard = view.findViewById(R.id.card_pressure)

        navToAccountButton = view.findViewById(R.id.navToAccountButton)

        fetchWeatherData()

        return view
    }

    // --- Fetch data from OpenWeatherMap ---
    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/3.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getOneCallWeather(lat, lon, apiKey, "metric", "minutely,alerts")

        call.enqueue(object : Callback<OneCallResponse> {
            override fun onResponse(call: Call<OneCallResponse>, response: Response<OneCallResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    updateUI(data)
                } else {
                    Toast.makeText(requireContext(), "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OneCallResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network Failure: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // --- Update the UI with fetched data ---
    private fun updateUI(data: OneCallResponse) {
        val current = data.current
        val daily = data.daily
        val hourly = data.hourly

        tvLocation.text = "Selangor, Malaysia"
        tvTemperature.text = "${current.temp.toInt()}°C"
        tvCondition.text = current.weather[0].description.replaceFirstChar { it.uppercase() }
        tvFeelsLike.text = "Feels like ${current.feels_like.toInt()}°C"
        tvHighLow.text = "H: ${daily[0].temp.max.toInt()}°  L: ${daily[0].temp.min.toInt()}°"

        // Weather icon
        val iconCode = current.weather[0].icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
        Glide.with(this).load(iconUrl).into(ivWeatherIcon)

        humidityCard.text = "${current.humidity}%"
        windCard.text = "${current.wind_speed} m/s"
        visibilityCard.text = "${current.visibility / 1000.0} km"
        pressureCard.text = "${current.pressure} hPa"

        // Inflate hourly items
        val inflater = LayoutInflater.from(requireContext())
        hourlyContainer.removeAllViews()
        val timeFormat = SimpleDateFormat("ha", Locale.getDefault())
        hourly.take(12).forEach { hour ->
            val view = inflater.inflate(R.layout.item_hourly_forecast, hourlyContainer, false)
            val time = view.findViewById<TextView>(R.id.tv_hour_time)
            val temp = view.findViewById<TextView>(R.id.tv_hour_temp)
            val icon = view.findViewById<ImageView>(R.id.iv_hour_icon)

            time.text = timeFormat.format(Date(hour.dt * 1000))
            temp.text = "${hour.temp.toInt()}°"
            val hourIconUrl = "https://openweathermap.org/img/wn/${hour.weather[0].icon}@2x.png"
            Glide.with(this).load(hourIconUrl).into(icon)
            hourlyContainer.addView(view)
        }

        // Inflate daily items
        dailyContainer.removeAllViews()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        daily.take(7).forEach { day ->
            val view = inflater.inflate(R.layout.item_daily_forecast, dailyContainer, false)
            val date = view.findViewById<TextView>(R.id.tv_day_name)
            val temp = view.findViewById<TextView>(R.id.tv_day_temp)
            val icon = view.findViewById<ImageView>(R.id.iv_day_icon)

            date.text = dayFormat.format(Date(day.dt * 1000))
            temp.text = "${day.temp.max.toInt()}° / ${day.temp.min.toInt()}°"
            val dayIconUrl = "https://openweathermap.org/img/wn/${day.weather[0].icon}@2x.png"
            Glide.with(this).load(dayIconUrl).into(icon)
            dailyContainer.addView(view)
        }
    }
}