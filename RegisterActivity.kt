package com.example.gptlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
// RegisterActivity.kt

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginTextView : TextView
    private lateinit var confirmPasswordEditText : EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // Initialize views after setContentView
        registerButton = findViewById(R.id.registerButton)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginTextView = findViewById(R.id.loginTextView)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)

        registerButton.setOnClickListener {

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
            } else if (!isEmailValid(email)) {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show()
            } else if (confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please confirm your password.", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registration success
//                            startActivity(Intent(this, MainActivity::class.java))
//                            finish()

                            //new
                            navigateTologin()

                        } else {
                            // If registration fails, display a message to the user.
                       //     Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT).show()

                            //new
                            // If registration fails, display a message to the user.
                            val errorMessage = task.exception?.message ?: "Registration failed."
                            if (errorMessage.contains("email address is already in use")) {
                                Toast.makeText(this, "An account with this email already exists.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }
        loginTextView.setOnClickListener{
            navigateTologin()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()

        //new
        navigateTologin()

        // Go back to LoginActivity when the back button is pressed
//        val intent = Intent(this, LoginActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        finish()
    }
    private fun navigateTologin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}

