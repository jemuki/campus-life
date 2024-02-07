package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.google.gson.GsonBuilder
import com.jemuki.ImageModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import kotlinx.android.synthetic.main.activity_addpost.*
import org.w3c.dom.Text
import kotlinx.android.synthetic.main.activity_singlemeet.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class singlemeet : AppCompatActivity() {
    lateinit var imageList: ArrayList<ImageModel>
    lateinit var imageAdapter: ImageAdapter
    lateinit var commentList: ArrayList<CommentModel>
    lateinit var commentAdapter: CommentAdapter
    lateinit var recyclerView: RecyclerView
    private var images: ArrayList<String?>? = null
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlemeet)
        val back = findViewById<ImageButton>(R.id.smeettoback)
        back.setOnClickListener {
            finish()
        }

        val prefs = getSharedPreferences("meetdb", Context.MODE_PRIVATE)
        val id = prefs.getString("id", "")
        val username = findViewById<TextView>(R.id.userz)
        username.text = prefs.getString("username", "")
        val post = findViewById<TextView>(R.id.themeetpost)
        post.text = prefs.getString("post", "")
        val likes = findViewById<TextView>(R.id.meetnumlikes)
        likes.text = prefs.getString("likes", "")
        val comments = findViewById<TextView>(R.id.meetnumcomments)
        comments.text = prefs.getString("comments", "")

        imageList=ArrayList<ImageModel>()
        images = ArrayList()
        if (prefs.getString("image1", "")!!.isEmpty()){
            imageswitcher.visibility = View.GONE
        }else {
//            imageList.add(ImageModel(prefs.getString("image1", "")))
//            imageList.add(ImageModel(prefs.getString("image2", "")))
//            imageList.add(ImageModel(prefs.getString("image3", "")))
//            imageList.add(ImageModel(prefs.getString("image4", "")))
//            imageList.add(ImageModel(prefs.getString("image5", "")))
//            imageList.add(ImageModel(prefs.getString("image6", "")))
//            imageList.add(ImageModel(prefs.getString("image7", "")))
//            imageList.add(ImageModel(prefs.getString("image8", "")))
//            imageList.add(ImageModel(prefs.getString("image9", "")))
//            imageList.add(ImageModel(prefs.getString("image10", "")))
            //            imageAdapter = ImageAdapter(applicationContext, imageList)
//            imageAdapter.setImageListItems(imageList)
//            viewPager.adapter = imageAdapter


            images!!.add(prefs.getString("image1", ""))
            images!!.add(prefs.getString("image2", ""))
            images!!.add(prefs.getString("image3", ""))
            images!!.add(prefs.getString("image4", ""))
            images!!.add(prefs.getString("image5", ""))
            images!!.add(prefs.getString("image6", ""))
            images!!.add(prefs.getString("image7", ""))
            images!!.add(prefs.getString("image8", ""))
            images!!.add(prefs.getString("image9", ""))
            images!!.add(prefs.getString("image10", ""))

            imageswitcher.setFactory { ImageView(applicationContext) }

            //next button
                isnext.setOnClickListener {
                    if (position < images!!.size-1){
                        position++
//                        imageSwitcher.setImageURI(images!![position])
                      //  Picasso.with(applicationContext).load(images!![position]).fit().into(imageswitcher)
                        Glide.with(applicationContext).load(images!![position]).into(imageswitcher)
                    }
                    else{
                        //no more images
                        Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
                    }
                }

            //previous button
            isback.setOnClickListener{
                if (position > 0){
                    position--
                    Picasso.with(applicationContext).load(images!![position]).fit().into(imageswitcher)
                }
                else{
                    //no more images
                    Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
                }
            }



            }//ends else


        //like the post
        val prefs2 = getSharedPreferences("db", Context.MODE_PRIVATE)
        val email = prefs2.getString("email", "")
        val  dp = prefs2.getString("dp", "")
        val namezip = prefs2.getString("username", "")
        val likeit = findViewById<ImageButton>(R.id.likemeet)
        likeit.setOnClickListener {
            val client = AsyncHttpClient(true, 80, 443)
            val data = JSONObject()
            data.put("id", id)
            data.put("email", email)

            val condata = StringEntity(data.toString())

            //post it to api
            client.post(this, "http://coding.co.ke/jereson/likemeetpost",
                condata, "application/json",
                object: JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        Toast.makeText(applicationContext, "$response", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseString: String?,
                        throwable: Throwable?
                    ) {
                        Toast.makeText(applicationContext, "Something went wrong "+statusCode, Toast.LENGTH_LONG).show()


                    }


                })//ends the client.post

        }//ends like it on click

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //val myLdt = LocalDateTime.of(year, month, day, ... )
        val adate = day.toString()+"/"+month.toString()+"/"+year.toString()


        val no = findViewById<TextView>(R.id.nok)
        no.visibility = View.GONE
        //get the comments
        recyclerView = findViewById(R.id.recyclermeetcomments)
        commentList=ArrayList<CommentModel>()

        val client = AsyncHttpClient(true, 80, 443)
        commentAdapter = CommentAdapter(applicationContext, commentList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/getmeetcommets/$id"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<CommentModel>::class.java).toList()
                    if (List.size == 0){
                       no.visibility =  View.VISIBLE
                        }else {
                        commentAdapter.setCommentListItems(List)

                         }
                }


                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(applicationContext, "Try again "+statusCode, Toast.LENGTH_LONG).show()

                }
            })//ends handler

        recyclerView.adapter = commentAdapter


        val prefsx = getSharedPreferences("db", Context.MODE_PRIVATE)
        val dpx = prefsx.getString("dp", "")

        //add a comment
        val com  = findViewById<EditText>(R.id.addmeetcom)
        val send = findViewById<ImageButton>(R.id.nowsendmeetcom)
        send.setOnClickListener {
            val client = AsyncHttpClient(true, 80, 443)
            val data = JSONObject()
            data.put("id", id)
            data.put("email", email)
            data.put("dp", dpx)
            data.put("username", namezip)
            data.put("comment", com.text)
            data.put("date", adate)

            val condata = StringEntity(data.toString())

            //post it to api
            client.post(this, "http://coding.co.ke/jereson/addmeetcomment",
                condata, "application/json",
                object: JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        Toast.makeText(applicationContext, "comment added", Toast.LENGTH_LONG).show()
                        com.text.clear()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseString: String?,
                        throwable: Throwable?
                    ) {
                        Toast.makeText(applicationContext, "Something went wrong "+statusCode, Toast.LENGTH_LONG).show()


                    }


                })//ends the client.post

        }//ends semd on click listener



    }//ends omcreate
}//ends class

private fun RequestCreator.into(imageswitcher: ImageSwitcher?) {
    TODO("Not yet implemented")
}

private fun RequestCreator.into(factory: Unit) {

}

private fun <TranscodeType> RequestBuilder<TranscodeType>.into(imageswitcher: ImageSwitcher?) {

}
