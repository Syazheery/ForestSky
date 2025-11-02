package com.example.forecastskyapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomnavigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    true
                }

                R.id.nav_account -> {
                    Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AccountFragment())
                        .commit()
                    true
                }

                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingsFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}
