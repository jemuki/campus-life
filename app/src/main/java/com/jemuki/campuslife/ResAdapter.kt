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
import com.squareup.picasso.Picasso


class ResAdapter(var context: Context, hostelList: ArrayList<ResModel>):
    RecyclerView.Adapter<ResAdapter .ViewHolder>(){

    var hostelList : List<ResModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hostel, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: ResAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.hosname) as TextView
        val loc = holder.itemView.findViewById(R.id.hoslocation) as TextView
        val image = holder.itemView.findViewById(R.id.hosimg) as ImageView

        val item = hostelList[position]
        name.text = item.name
        loc.text = item.location



        Glide.with(context).load(item.img1)
            .apply(RequestOptions().centerCrop())
            .into(image)
        holder.itemView.setOnClickListener {
            //save the clicked product on shared preference
            val prefs = context.getSharedPreferences("hosteldb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("name", item.name)
            editor.putString("location", item.location)
            editor.putString("desc", item.desc)
            editor.putString("price", item.price)
            editor.putString("phone", item.phone)
            editor.putString("image1", item.img1)
            editor.putString("image2", item.img2)
            editor.putString("image3", item.img3)
            editor.putString("image4", item.img4)
            editor.putString("image5", item.img5)
            editor.putString("image6", item.img6)
            editor.putString("image7", item.img7)
            editor.putString("image8", item.img8)
            editor.putString("image9", item.img9)
            editor.putString("image10", item.img10)

            editor.apply()

            //intent
            val x = Intent(context, singlehostel::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)

        }//ends holder.item view
    }

    override fun getItemCount(): Int {
        return hostelList.size
    }

    //we will call this function on Loopj response
    fun setHostelListItems(hostelList: List<ResModel>){
        this.hostelList = hostelList
        notifyDataSetChanged()
    }
}//end class
