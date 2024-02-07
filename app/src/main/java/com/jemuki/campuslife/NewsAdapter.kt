package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso


class NewsAdapter(var context: Context, newsList: ArrayList<NewsModel>):
    RecyclerView.Adapter<NewsAdapter .ViewHolder>(){

    var newsList : List<NewsModel> = listOf() // empty news list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val news = holder.itemView.findViewById(R.id.thenews) as TextView
        val image = holder.itemView.findViewById(R.id.newscover) as ImageView


        val item = newsList[position]
        if (item.image1.isEmpty()){
            news.text = item.news
            image.visibility = View.GONE

        }else {
            news.text = item.news
            Glide.with(context).load(item.image1)
                .apply(RequestOptions().centerCrop())
                .into(image)
        }



        holder.itemView.setOnClickListener {
            //save the clicked product on shared preference
            val prefs = context.getSharedPreferences("newsdb", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("id", item.id)
            editor.putString("news", item.news)
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
            val x = Intent(context, singlenews::class.java)
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(x)

        }//ends holder.item view
        holder.itemView.findViewById<ImageButton>(R.id.comnews).setOnClickListener {
            val prefs2 = context.getSharedPreferences("newsdb", Context.MODE_PRIVATE)
            val editor2 = prefs2.edit()
            editor2.putString("id", item.id)
            editor2.putString("news", item.news)
            editor2.putString("image1", item.image1)
            editor2.putString("image2", item.image2)
            editor2.putString("image3", item.image3)
            editor2.putString("image4", item.image4)
            editor2.putString("image5", item.image5)
            editor2.putString("image6", item.image6)
            editor2.putString("image7", item.image7)
            editor2.putString("image8", item.image8)
            editor2.putString("image9", item.image9)
            editor2.putString("image10", item.image10)
            editor2.apply()

            val y = Intent(context, singlenews::class.java)
            y.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(y)

        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    //we will call this function on Loopj response
    fun setNewsListItems(newsList: List<NewsModel>){
        this.newsList = newsList
        notifyDataSetChanged()
    }
}//end class
