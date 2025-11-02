package com.example.forecastskyapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    private lateinit var radioCelsius: RadioButton
    private lateinit var radioFahrenheit: RadioButton
    private lateinit var switchNotifications: Switch
    private lateinit var switchDarkMode: Switch
    private lateinit var editTextLocation: EditText
    private lateinit var buttonSave: Button
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize SharedPreferences
        sharedPref = requireContext().getSharedPreferences("WeatherSettings", Context.MODE_PRIVATE)

        // Initialize views
        radioCelsius = view.findViewById(R.id.radioCelsius)
        radioFahrenheit = view.findViewById(R.id.radioFahrenheit)
        switchNotifications = view.findViewById(R.id.switchNotifications)
        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        editTextLocation = view.findViewById(R.id.editTextLocation)
        buttonSave = view.findViewById(R.id.buttonSaveSettings)

        // Load saved settings
        loadSettings()

        // Save button click
        buttonSave.setOnClickListener {
            saveSettings()
            Toast.makeText(requireContext(), "Settings saved successfully", Toast.LENGTH_SHORT).show()
        }

        // Dark mode toggle listener
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return view
    }

    private fun saveSettings() {
        val editor = sharedPref.edit()

        val temperatureUnit = if (radioCelsius.isChecked) "C" else "F"
        val notificationsEnabled = switchNotifications.isChecked
        val darkModeEnabled = switchDarkMode.isChecked
        val location = editTextLocation.text.toString()

        editor.putString("TemperatureUnit", temperatureUnit)
        editor.putBoolean("NotificationsEnabled", notificationsEnabled)
        editor.putBoolean("DarkModeEnabled", darkModeEnabled)
        editor.putString("DefaultLocation", location)
        editor.apply()
    }

    private fun loadSettings() {
        val temperatureUnit = sharedPref.getString("TemperatureUnit", "C")
        val notificationsEnabled = sharedPref.getBoolean("NotificationsEnabled", true)
        val darkModeEnabled = sharedPref.getBoolean("DarkModeEnabled", false)
        val location = sharedPref.getString("DefaultLocation", "")

        // Set the UI based on saved preferences
        if (temperatureUnit == "C") {
            radioCelsius.isChecked = true
        } else {
            radioFahrenheit.isChecked = true
        }

        switchNotifications.isChecked = notificationsEnabled
        switchDarkMode.isChecked = darkModeEnabled
        editTextLocation.setText(location)

        // Apply dark mode
        if (darkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
