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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //Remember The User
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, TweetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun signIn(view: View){
        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        if(email != "" && password != ""){
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    val currentUser = auth.currentUser?.displayName.toString()
                    Toast.makeText(applicationContext,"Welcome back ${currentUser}", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, TweetActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun toSignUp(view: View){
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

    fun toForgotPassword(view: View){
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
        //finish()
    }


}