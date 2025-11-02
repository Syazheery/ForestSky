package com.example.forecastskyapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userEmailTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        auth = FirebaseAuth.getInstance()
        userEmailTextView = view.findViewById(R.id.userEmailTextView)
        logoutButton = view.findViewById(R.id.logoutButton)

        displayUserInfo()

        logoutButton.setOnClickListener {
            performLogout()
        }

        return view
    }

    private fun displayUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            userEmailTextView.text = user.email ?: "User ID: ${user.uid}"
        } else {
            userEmailTextView.text = "Error: Not logged in."
            Toast.makeText(requireContext(), "You must be logged in to view this page.", Toast.LENGTH_SHORT).show()

            // Redirect to login screen if not authenticated
            val intent = Intent(requireContext(), AccountLogin::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun performLogout() {
        auth.signOut()
        Toast.makeText(requireContext(), "Logged out successfully.", Toast.LENGTH_SHORT).show()

        // Navigate back to Login or MainActivity
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
