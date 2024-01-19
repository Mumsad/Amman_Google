package com.example.gptlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var logoutButton: Button
    private lateinit var userEmailTextView: TextView
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        userEmailTextView = findViewById(R.id.userEmailTextView)

        // Get the current user from Firebase
        val currentUser = auth.currentUser

        // Check if the user is signed in
        if (currentUser != null) {
            // User is signed in
            val userEmail = currentUser.email
            userEmailTextView.text = "Welcome, $userEmail"
        } else {
            // User is not signed in (handle this case accordingly)

            // or show a message that the user is not authenticated.
            userEmailTextView.text = "Welcome, Guest. Please sign in."

            // For example, you might want to redirect to the login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        logoutButton = findViewById(R.id.MainlogoutButton)

        // Logout button click listener
        logoutButton.setOnClickListener {

//                auth.signOut()
//                startActivity(Intent(this,LoginActivity::class.java))
//                finish()

            //new
            signOutFirebase()
            signOutGoogleSignIn()
        }
    }

    private fun signOutFirebase() {
        auth.signOut()
    }
    private fun signOutGoogleSignIn() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // Redirect to the login screen
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
}