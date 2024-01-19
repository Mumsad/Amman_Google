package com.example.gptlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerTextView: TextView
    private lateinit var googleSignInButton: Button
    private lateinit var forgotPasswordTextView : TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)


        // Initialize views after setContentView
        loginButton = findViewById(R.id.LloginButton)
        emailEditText = findViewById(R.id.LemailEditText)
        passwordEditText = findViewById(R.id.LpasswordEditText)
        registerTextView = findViewById(R.id.LregisterTextView)
        googleSignInButton = findViewById(R.id.googleSignInButton)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)

        forgotPasswordTextView.setOnClickListener{
            navigateToPasswordReset()
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim{it <= ' '}
            val password = passwordEditText.text.toString().trim{it <= ' '}

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
            }
            else if (!isEmailValid(email)) {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            }
            else if(password.length < 8){
                Toast.makeText(this,"Password should be at least 8 characters long",Toast.LENGTH_LONG).show()
            }
            else
            {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        // Login success
                        Toast.makeText(this,"You are logged in Successfully.",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        // Login success
//                        Log.d("LoginActivity", "Login successful, starting MainActivity")
//                        startMainActivity()
                    } else {
                        // If login fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                        Log.w("LoginActivity", "Login failed", task.exception)
//                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
         }
        }
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (auth.currentUser != null) {
            // User is already logged in, go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun navigateToPasswordReset() {
        val intent = Intent(this,PasswordResetActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Handle the result of Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign-In failed
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                    // Update UI or navigate to the next screen
                    // ...
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    // Update UI or handle the failure
                    // ...
                }
            }
    }
    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
    }
}
fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}
