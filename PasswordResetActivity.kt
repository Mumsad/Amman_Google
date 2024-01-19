package com.example.gptlogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var ResetemailEditText: EditText
    private lateinit var ResetresetButton: Button
    private lateinit var ResetbackToLoginTextView : TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        auth = FirebaseAuth.getInstance()

        ResetemailEditText = findViewById(R.id.ResetemailEditText)
        ResetresetButton = findViewById(R.id.ResetresetButton)
        ResetbackToLoginTextView = findViewById(R.id.ResetbackToLoginTextView)

        ResetresetButton.setOnClickListener {
            val email = ResetemailEditText.text.toString().trim()

            if(email.isNotEmpty()){
                sendPasswordResetEmail(email)
                Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
        // Set a click listener for the backToLoginTextView
        ResetbackToLoginTextView.setOnClickListener {
            backToLogin()
        }
    }

    private fun sendPasswordResetEmail(email: String) {

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    // Password reset email sent successfully
                    Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                    // You may navigate to a confirmation screen or handle the success case
                }else{
                    // If password reset fails, display a message to the user.
                    // You can handle specific error cases using task.exception
                    val errorMessage = task.exception?.message ?: "Password reset failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
    // Function to go back to the login screen
    private fun backToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        // Override the back button behavior to go back to the login screen
        backToLogin()
    }
}