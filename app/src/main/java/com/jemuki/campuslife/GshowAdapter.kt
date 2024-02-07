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


class GshowAdapter(var context: Context, gshowList: ArrayList<GshowModel>):
    RecyclerView.Adapter<GshowAdapter .ViewHolder>(){

    var gshowList : List<GshowModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GshowAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_school, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: GshowAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.name) as TextView
        val slogan = holder.itemView.findViewById(R.id.slogan) as TextView
        val image = holder.itemView.findViewById(R.id.cover) as ImageView

        val item = gshowList[position]
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
            val prefs = context.getSharedPreferences("gshowdb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("name", item.name)
            editor.apply()

            //intent
            val x = Intent(context, groups::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)
            Toast.makeText(context, "swipe left for menu", Toast.LENGTH_LONG).show()

        }//ends holder.item view
    }

    override fun getItemCount(): Int {
        return gshowList.size
    }

    //we will call this function on Loopj response
    fun setGshowListItems(gshowList: List<GshowModel>){
        this.gshowList = gshowList
        notifyDataSetChanged()
    }
}//end class
