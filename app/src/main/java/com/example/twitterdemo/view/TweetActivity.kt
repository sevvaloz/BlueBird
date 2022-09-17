package com.example.twitterdemo.view

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterdemo.R
import com.example.twitterdemo.adapter.TweetAdapter
import com.example.twitterdemo.model.Tweet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_tweet.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TweetActivity : AppCompatActivity(){

    private  lateinit var recyclerViewAdapter : TweetAdapter

    var tweetList = ArrayList<Tweet>()

    private lateinit var auth: FirebaseAuth

    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore

    //Menuyu MainActivity'e baglamak
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Menuden bir item secildiginde ne yapacagimizi belirlemek
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.log_out){
            auth.signOut()

            Toast.makeText(applicationContext, "Log out successfully", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if(item.itemId == R.id.delete_user){
            val intent = Intent(this, ReAuthenticateActivity::class.java)
            startActivity(intent)
            //finish()
        }
        if(item.itemId == R.id.share_tweet){
            val intent = Intent(this, ShareTweetActivity::class.java)
            startActivity(intent)
            //finish yapmadık yani activity'i kapatmadık çünkü: kullanıcı geri gelmek istediğinde uygulamadan çıkış yapsın istemiyoruz.
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)

        // Initialize Firebase Auth
        auth = Firebase.auth

        FirebaseGetData()

        val layoutmanager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutmanager
        recyclerViewAdapter = TweetAdapter(tweetList)
        recyclerView.adapter = recyclerViewAdapter

    }


    //database'den verileri surekli olarak okumak (snapshot)
    fun FirebaseGetData(){
        db.collection("Tweets").orderBy("TweetDate", Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if(error != null){
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            }
            else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){
                        val documents = snapshot.documents
                        tweetList.clear()
                        for(document in documents){
                            val userName = document.get("UserName") as String //get fonksiyonu any tipinde geri dondugu icin string'e cevirdik
                            val tweet = document.get("Tweet") as String
                            val tweetDate = document.get("TweetDate") as Any
                            val image = document.get("Image") as String?

                            var savedTweet = Tweet(userName, tweet, tweetDate, image)
                            tweetList.add(savedTweet)

                        }
                        recyclerViewAdapter.notifyDataSetChanged() //yeni veri geldiginden haberdar et
                    }
                }
            }
        }
    }


}

