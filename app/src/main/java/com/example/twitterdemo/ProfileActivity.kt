package com.example.twitterdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.twitterdemo.adapter.TweetAdapter
import com.example.twitterdemo.model.Photo
import com.example.twitterdemo.model.Tweet
import com.example.twitterdemo.view.TweetActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    val db = Firebase.firestore
    val storage = Firebase.storage
    var strUrl : String = ""
    var image : Uri? = null
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    fun applyManagement(view: View){

        val uuid = UUID.randomUUID()
        var pp = "${uuid}.jpg"

        val referance = storage.reference   //referance değişkeni şuan deponun kendisine referans veriyor
        val profilePictureRef = referance.child("images").child(pp)

        if(image != null){
            profilePictureRef.putFile(image!!).addOnSuccessListener {
                //url alma
                profilePictureRef.downloadUrl.addOnSuccessListener {
                    strUrl = it.toString()
                    val photo = hashMapOf<String, String>()
                    val name = Firebase.auth.currentUser!!.displayName.toString()
                    photo.put("Url", strUrl)
                    photo.put("UserName", name)
                    db.collection("ProfilePictures").add(photo).addOnCompleteListener {
                        if(it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
                Toast.makeText(applicationContext,"Change applied", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }



    fun managePp(view: View) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //izin verilmedi, izin iste
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            //izin zaten verilmiş, direkt galeriye git
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

        override fun onRequestPermissionsResult(requestCode: Int, permission: Array<out String>, grantResult: IntArray) {
            if(requestCode == 1) {
                if(grantResult.isNotEmpty() && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent,2)
                }
            }
            super.onRequestPermissionsResult(requestCode, permission, grantResult)
        }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
                image = data.data
                if(image != null) {
                    if(Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(this.contentResolver, image!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        profilePicture.setImageBitmap(bitmap)
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image)
                        profilePicture.setImageBitmap(bitmap)
                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }


}