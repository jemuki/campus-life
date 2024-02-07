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
import com.jemuki.GroupModel
import com.squareup.picasso.Picasso


class GroupAdapter(var context: Context, groupList: ArrayList<GroupModel>):
    RecyclerView.Adapter<GroupAdapter .ViewHolder>(){

    var groupList : List<GroupModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: GroupAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.groupname) as TextView
        val image = holder.itemView.findViewById(R.id.groupicon) as ImageView

        val item = groupList[position]
        name.text = item.name



        Picasso
            .with(context)
            .load(item.icon)
            .fit()
            // call .centerInside() or .centerCrop() to avoid a stretched image
            .into(image);
        holder.itemView.setOnClickListener {
            //save the clicked product on shared preference
            val prefs = context.getSharedPreferences("groupdb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("name", item.name)
            editor.putString("icon", item.icon)
            editor.apply()

            //intent
            val x = Intent(context, singlegroup::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)

        }//ends holder.item view
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    //we will call this function on Loopj response
    fun setGroupListItems(groupList: List<GroupModel>){
        this.groupList = groupList
        notifyDataSetChanged()
    }
}//end class
