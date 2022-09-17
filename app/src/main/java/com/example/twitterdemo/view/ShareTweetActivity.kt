package com.example.twitterdemo.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.twitterdemo.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_share_tweet.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.seconds


class ShareTweetActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_tweet)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun share(view: View){
        //view'dan bilgileri alma
        val tweet = shareTweetText.text.toString()
        val name = auth.currentUser!!.displayName.toString()
        val date = Timestamp.now()

        //date ayarlama
        val net_date = date.toDate()
        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a", Locale.ENGLISH)
        val sdf_date = sdf.format(net_date)

        //hashmap olusturma
        val user = hashMapOf<String, Any>()
        user.put("Tweet", tweet)
        user.put("UserName", name)
        user.put("TweetDate", sdf_date)


        //database, collection, document olusturma
        if(tweet != ""){
            db.collection("Tweets").add(user).addOnCompleteListener {
                if(it.isSuccessful){
                    finish() //ShareTweetActivity'i kapat
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else{
            Toast.makeText(this, "Please write something!", Toast.LENGTH_LONG).show()
        }

    }


}