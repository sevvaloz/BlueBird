package com.example.twitterdemo.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twitterdemo.ProfileActivity
import com.example.twitterdemo.R
import com.example.twitterdemo.adapter.TweetAdapter
import com.example.twitterdemo.model.Photo
import com.example.twitterdemo.model.Tweet
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import kotlinx.android.synthetic.main.activity_tweet.*
import kotlinx.android.synthetic.main.recycler_row.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class TweetActivity : AppCompatActivity(){

    private  lateinit var recyclerViewAdapter : TweetAdapter

    var tweetList = ArrayList<Tweet>()
    var photoList = ArrayList<Photo>()

    private lateinit var auth: FirebaseAuth

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
        if(item.itemId == R.id.profile){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)

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


    //firebase'den tweet silme??? ÇALIŞMIYOR
    fun DeleteTweet(){
        db.collection("Tweets").addSnapshotListener { snapshot, error ->
            val documents = snapshot!!.documents
            if(snapshot == null){
                for(document in documents){

                    val userName = document.get("UserName") as String
                    val tweet = document.get("Tweet") as String
                    val tweetDate = document.get("TweetDate") as Any
                    val image = document.get("Image") as String?

                    var deletedTweet = Tweet(userName, tweet, tweetDate, image)
                    tweetList.remove(deletedTweet)

                }
                recyclerViewAdapter.notifyDataSetChanged()
            }


        }

    }






}



