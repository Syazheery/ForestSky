package com.example.forecastskyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button // <-- Import the Button class
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val apiKey = "fc303828110a08baf778cdae2503ae44"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        // --- START OF CHANGES ---

        // 1. Find the button from the layout by its ID
        val accountButton = findViewById<Button>(R.id.navToAccountButton)

        // 2. Set an OnClickListener on the button
        accountButton.setOnClickListener {
            // 3. Define what happens on click: navigate to AccountLogin
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }
        // --- END OF CHANGES ---
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        if (auth.currentUser == null) {
            // User is not signed in, redirect to the Login page
            val intent = Intent(this, AccountLogin::class.java)
            startActivity(intent)
            // Prevent the user from navigating back to this activity without logging in
            finish()
        } else {
            // User is signed in, proceed to fetch weather data
            fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        val tempText = findViewById<TextView>(R.id.tempText)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeather("Kuala Lumpur", apiKey, "metric")

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val temp = response.body()?.main?.temp
                    tempText.text = "Temperature: $temp°C"
                } else {
                    tempText.text = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tempText.text = "Failed: ${t.message}"
            }
        })
    }
}
