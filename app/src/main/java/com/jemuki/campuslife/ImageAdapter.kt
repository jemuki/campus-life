package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jemuki.ImageModel
import com.squareup.picasso.Picasso


class ImageAdapter(var context: Context, imageList: ArrayList<ImageModel>):
    RecyclerView.Adapter<ImageAdapter .ViewHolder>(){

    var imageList : List<ImageModel> = listOf() // empty product list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_item, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {

        val image = holder.itemView.findViewById(R.id.idIVImage) as ImageView

        val item = imageList[position]



        Glide.with(context).load(item.image)
            .apply(RequestOptions().centerCrop())
            .into(image)
             image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

    override fun getItemCount(): Int {
        return imageList.size
    }

    //we will call this function on Loopj response
    fun setImageListItems(imageList: List<ImageModel>){
        this.imageList = imageList
        notifyDataSetChanged()
    }
}//end class
