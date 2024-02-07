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
import com.squareup.picasso.Picasso


class MeetshowAdapter(var context: Context, mshowist: ArrayList<MeetshowModel>):
    RecyclerView.Adapter<MeetshowAdapter .ViewHolder>(){

    var mshowList : List<MeetshowModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetshowAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_school, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: MeetshowAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.name) as TextView
        val slogan = holder.itemView.findViewById(R.id.slogan) as TextView
        val image = holder.itemView.findViewById(R.id.cover) as ShapeableImageView

        val item = mshowList[position]
        name.text = item.name
        slogan.text = item.slogan



        Picasso
            .with(context)
            .load(item.cover)
            .fit()
            // call .centerInside() or .centerCrop() to avoid a stretched image
            .into(image);

        holder.itemView.setOnClickListener {
            //save the clicked product on shared preference
            val prefs = context.getSharedPreferences("mshowdb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("name", item.name)
            editor.apply()

            //intent
            val x = Intent(context, meet::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)

        }//ends holder.item view
    }

    override fun getItemCount(): Int {
        return mshowList.size
    }

    //we will call this function on Loopj response
    fun setMshowListItems(mshowList: List<MeetshowModel>){
        this.mshowList = mshowList
        notifyDataSetChanged()
    }
}//end class
