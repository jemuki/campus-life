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


class CourseAdapter(var context: Context, courseList: ArrayList<CourseModel>):
    RecyclerView.Adapter<CourseAdapter .ViewHolder>(){

    var courseList : List<CourseModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: CourseAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.coursename) as TextView

        val item = courseList[position]
        name.text = item.name



        holder.itemView.setOnClickListener {
            //save the clicked product on shared preference
            val prefs = context.getSharedPreferences("coursedb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("cover", item.cover)
            editor.putString("name", item.name)
            editor.putString("goal", item.goal)
            editor.putString("opportunities", item.opp)
            editor.putString("duration", item.duration)
            editor.putString("qualification", item.qualification)
            editor.putString("outcome", item.outcome)

            editor.apply()

            //intent
            val x = Intent(context, singlecourse::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)

        }//ends holder.item view
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    //we will call this function on Loopj response
    fun setCourseListItems(courseList: List<CourseModel>){
        this.courseList = courseList
        notifyDataSetChanged()
    }
}//end class
