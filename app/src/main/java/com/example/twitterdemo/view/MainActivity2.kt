package com.example.twitterdemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.twitterdemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }


    fun signUp(view: View){
        val email = emailText2.text.toString()
        val password = passwordText2.text.toString()
        val username = usernameText.text.toString()

        if(email != "" && password != ""){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { it ->
                if(it.isSuccessful) {

                    //Update Username
                    val currentUser = auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }
                    currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(applicationContext, "Account created successfully", Toast.LENGTH_LONG).show()
                        }
                    }

                    val intent = Intent(this, TweetActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun toSignIn(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}