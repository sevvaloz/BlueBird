package com.example.twitterdemo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.twitterdemo.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_re_authenticate.*

class ReAuthenticateActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_authenticate)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun deleteAccount(view: View){
        val currentUser = auth.currentUser
        val email = reauthEmail.text.toString()
        val password = reauthPassword.text.toString()
        val credential = EmailAuthProvider.getCredential(email, password)


        currentUser!!.reauthenticate(credential).addOnCompleteListener {
            if(it.isSuccessful){
                currentUser.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(applicationContext, "Account deleted", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


}