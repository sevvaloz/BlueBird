package com.example.twitterdemo.adapter

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterdemo.model.Photo
import com.example.twitterdemo.model.Tweet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*


class TweetAdapter(val tweetlist : ArrayList<Tweet>) : RecyclerView.Adapter<TweetAdapter.Holder>(){


    class Holder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        override fun onClick(p0: View?) {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(com.example.twitterdemo.R.layout.recycler_row,parent,false)
        return Holder(view)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        //tweet gösterme
        holder.itemView.recycler_row_user_name.text = tweetlist[position].username
        holder.itemView.recycler_row_tweet.text = tweetlist[position].usertweet
        holder.itemView.recycler_row_tweet_date.text = tweetlist[position].tweetdate.toString()
    }

    override fun getItemCount(): Int {
        return tweetlist.size
    }

}
