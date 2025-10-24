package com.example.forecastskyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userEmailTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = FirebaseAuth.getInstance()
        userEmailTextView = findViewById(R.id.userEmailTextView)
        logoutButton = findViewById(R.id.logoutButton)

        displayUserInfo()

        logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun displayUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            userEmailTextView.text = user.email ?: "User ID: ${user.uid}"
        } else {
            userEmailTextView.text = "Error: Not logged in."
            Toast.makeText(this, "You must be logged in to view this page.", Toast.LENGTH_SHORT).show()
            // Redirect to login if somehow reached this page unauthenticated
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performLogout() {
        auth.signOut()
        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show()
        // Navigate back to the Login screen
        val intent = Intent(this, MainActivity::class.java)
        // Clear the activity stack so they can't hit 'back' to return
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
