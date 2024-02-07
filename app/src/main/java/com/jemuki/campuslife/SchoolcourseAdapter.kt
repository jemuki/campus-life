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


class SchoolcourseAdapter(var context: Context, schoolcourseList: ArrayList<SchoolcourseModel>):
    RecyclerView.Adapter<SchoolcourseAdapter .ViewHolder>(){

    var schoolcourseList : List<SchoolcourseModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolcourseAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_school, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: SchoolcourseAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.name) as TextView
        val slogan = holder.itemView.findViewById(R.id.slogan) as TextView
        val image = holder.itemView.findViewById(R.id.cover) as ImageView

        val item = schoolcourseList[position]
        name.text = item.name
        slogan.text = item.slogan



        Glide.with(context).load(item.cover)
            .apply(RequestOptions().centerCrop())
            .into(image)
        holder.itemView.setOnClickListener {
            //save the clicked product on shared preference
            val prefs = context.getSharedPreferences("schoolcoursedb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("name", item.name)
            editor.apply()

            //intent
            val x = Intent(context, courses::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)
            Toast.makeText(context, "swipe left for menu", Toast.LENGTH_LONG).show()

        }//ends holder.item view
    }

    override fun getItemCount(): Int {
        return schoolcourseList.size
    }

    //we will call this function on Loopj response
    fun setSchoolcourseListItems(schoolcourseList: List<SchoolcourseModel>){
        this.schoolcourseList = schoolcourseList
        notifyDataSetChanged()
    }
}//end class
