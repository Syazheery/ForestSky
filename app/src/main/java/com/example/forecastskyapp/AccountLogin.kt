package com.example.forecastskyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AccountLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var switchTextView: TextView
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_login) // your XML filename here

        // Initialize views
        auth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.btnLogin)
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        switchTextView = findViewById(R.id.switchTextView)

        if (auth.currentUser != null) {
            navigateToMain()
            return
        }

        updateUiForMode()

        btnLogin.setOnClickListener {
            if (isLoginMode) signIn() else register()
        }

        switchTextView.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUiForMode()
        }
    }

    private fun updateUiForMode() {
        if (isLoginMode) {
            btnLogin.text = "Sign In"
            switchTextView.text = "Need an account? Click here to Register."
        } else {
            btnLogin.text = "Register"
            switchTextView.text = "Already have an account? Click here to Sign In."
        }
    }

    private fun signIn() {
        val email = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign In Successful.", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun register() {
        val email = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (email.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Email required and password must be at least 6 characters.", Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration Successful. Logging in...", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close login activity so user can't go back
    }
}