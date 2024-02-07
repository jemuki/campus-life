package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.jemuki.GroupModel
import com.squareup.picasso.Picasso


class SinglegroupAdapter(var context: Context, grouppostList: ArrayList<SinglegroupModel>):
    RecyclerView.Adapter<SinglegroupAdapter .ViewHolder>(){

    var grouppostList : List<SinglegroupModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglegroupAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: SinglegroupAdapter.ViewHolder, position: Int) {
        val post = holder.itemView.findViewById(R.id.thepost) as TextView
        val user = holder.itemView.findViewById<TextView>(R.id.postusername)
        val likes = holder.itemView.findViewById<TextView>(R.id.postnumlikes)
        val comments = holder.itemView.findViewById<TextView>(R.id.postnumcomments)
        val image = holder.itemView.findViewById(R.id.image) as ShapeableImageView

        val item = grouppostList[position]
        if (item.image1.isEmpty()){
            post.text = item.post
            user.text = item.username
            likes.text = item.likes
            comments.text =item.comments
            image.visibility = View.GONE
        }//ends the if
       else {
            post.text = item.post
            user.text = item.username
            likes.text = item.likes
            comments.text =item.comments
            Picasso
                .with(context)
                .load(item.image1)
                .fit()
                // call .centerInside() or .centerCrop() to avoid a stretched image
                .into(image);

        }//ends the else





        holder.itemView.setOnClickListener {
            //save the clicked product on shared preference
            val prefs = context.getSharedPreferences("grouppostdb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("username", item.username)
            editor.putString("post", item.post)
            editor.putString("likes", item.likes)
            editor.putString("comments", item.comments)
            editor.putString("image1", item.image1)
            editor.putString("image2", item.image2)
            editor.putString("image3", item.image3)
            editor.putString("image4", item.image4)
            editor.putString("image5", item.image5)
            editor.putString("image6", item.image6)
            editor.putString("image7", item.image7)
            editor.putString("image8", item.image8)
            editor.putString("image9", item.image9)
            editor.putString("image10", item.image10)

            editor.apply()

            //intent
            val x = Intent(context, grouppost::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)

        }//ends holder.item view
    }

    override fun getItemCount(): Int {
        return grouppostList.size
    }

    //we will call this function on Loopj response
    fun setGrouppostListItems(grouppostList: List<SinglegroupModel>){
        this.grouppostList = grouppostList
        notifyDataSetChanged()
    }
}//end class
