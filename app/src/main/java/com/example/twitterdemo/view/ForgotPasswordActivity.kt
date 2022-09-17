package com.example.twitterdemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.twitterdemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun sendResetLink(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

        val emailAddress = emailTextForReset.text.toString()
        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Password reset link sent to your email", Toast.LENGTH_LONG).show()
                }
            }
    }
}