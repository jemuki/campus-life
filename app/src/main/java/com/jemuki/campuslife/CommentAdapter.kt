package com.jemuki.campuslife

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView


class CommentAdapter(var context: Context, commentList: ArrayList<CommentModel>):
    RecyclerView.Adapter<CommentAdapter .ViewHolder>(){

    var commentList : List<CommentModel> = listOf() // empty news list


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    //Note below code returns above class and pass the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments, parent, false)
        return ViewHolder(view)
    }


    //so far item view is same as single item
    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById(R.id.comname) as TextView
        val tcomm = holder.itemView.findViewById(R.id.thecomment) as TextView
        val image = holder.itemView.findViewById<ShapeableImageView>(R.id.commentdp)
        val item = commentList[position]
        name.text = item.name
        tcomm.text = item.comment

        Glide.with(context).load(item.dp)
            .apply(RequestOptions().centerCrop())
            .into(image)

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    //we will call this function on Loopj response
    fun setCommentListItems(commentList: List<CommentModel>){
        this.commentList = commentList
        notifyDataSetChanged()
    }
}//end class
