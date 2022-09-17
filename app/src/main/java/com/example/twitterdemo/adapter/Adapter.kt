package com.example.twitterdemo.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterdemo.R
import com.example.twitterdemo.model.Tweet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.recycler_row.view.*
import java.lang.reflect.Array.get
import java.util.zip.Inflater

class TweetAdapter(val tweetlist : ArrayList<Tweet>) : RecyclerView.Adapter<TweetAdapter.Holder>(){
    class Holder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //tweet gösterme
        holder.itemView.recycler_row_user_name.text = tweetlist[position].username
        holder.itemView.recycler_row_tweet.text = tweetlist[position].usertweet
        holder.itemView.recycler_row_tweet_date.text = tweetlist[position].tweetdate.toString()



        //tweet silme
        holder.itemView.recycler_row_delete_button.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Deleting tweet...")
            builder.setMessage("You sure to delete this tweet?")
            builder.setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->
                FirebaseDatabase.getInstance().reference.child("Tweets")
                    .child(getItemId(position).toString()).removeValue() //BU SATIR ÇALIŞMIYOR!!
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(holder.itemView.context,"Canceled", Toast.LENGTH_LONG).show()
            })
            builder.show()
        }

    }

    override fun getItemCount(): Int {
        return tweetlist.size
    }


}
