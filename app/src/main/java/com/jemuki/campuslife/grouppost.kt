package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.jemuki.ImageModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import kotlinx.android.synthetic.main.activity_grouppost.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class grouppost : AppCompatActivity() {
    lateinit var imageList: ArrayList<ImageModel>
    lateinit var imageAdapter: ImageAdapter
    lateinit var commentList: ArrayList<CommentModel>
    lateinit var commentAdapter: CommentAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grouppost)

        val back = findViewById<ImageButton>(R.id.sgposttoback)
        back.setOnClickListener {
            finish()
        }

        imageList=ArrayList<ImageModel>()

        val prefs = getSharedPreferences("grouppostdb", Context.MODE_PRIVATE)
        val id = prefs.getString("id", "")
        val username = findViewById<TextView>(R.id.spostusername)
        username.text = prefs.getString("username", "")
        val post = findViewById<TextView>(R.id.thespost)
        post.text = prefs.getString("post", "")
        val likes = findViewById<TextView>(R.id.postnumslikes)
        likes.text = prefs.getString("likes", "")
        val comments = findViewById<TextView>(R.id.postnumscomments)
        comments.text = prefs.getString("comments", "")

        if (prefs.getString("image1", "")!!.isEmpty()) {
            viewPager.visibility = View.GONE
        }//ends the if
        else {
            imageList.add(ImageModel(prefs.getString("image1", "")))
            imageList.add(ImageModel(prefs.getString("image2", "")))
            imageList.add(ImageModel(prefs.getString("image3", "")))
            imageList.add(ImageModel(prefs.getString("image4", "")))
            imageList.add(ImageModel(prefs.getString("image5", "")))
            imageList.add(ImageModel(prefs.getString("image6", "")))
            imageList.add(ImageModel(prefs.getString("image7", "")))
            imageList.add(ImageModel(prefs.getString("image8", "")))
            imageList.add(ImageModel(prefs.getString("image9", "")))
            imageList.add(ImageModel(prefs.getString("image10", "")))


            imageAdapter = ImageAdapter(applicationContext, imageList)
            imageAdapter.setImageListItems(imageList)
            viewPager.adapter = imageAdapter
        }//endsthe else


        //like the post
        val likebtn = findViewById<ImageButton>(R.id.likespost)
        val prefs2 = getSharedPreferences("db", Context.MODE_PRIVATE)
        val email = prefs2.getString("email", "")
        val username2 = prefs2.getString("username", "")
        val likedcolor = "#ffffff"
        likebtn.setOnClickListener {
            //post details for notifications table
            val client = AsyncHttpClient(true, 80, 443)
            val data = JSONObject()
            data.put("id", id)
            data.put("email", email)
            val condata = StringEntity(data.toString())

            //post it to api
            client.post(this, "http://coding.co.ke/jereson/likegrouppost",
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


        }//ends like btn on click


        val no = findViewById<TextView>(R.id.noz)
        no.visibility = View.GONE

        //get the posts comments
        recyclerView = findViewById(R.id.recyclergrouppostcomments)
        commentList = ArrayList<CommentModel>()

        val client = AsyncHttpClient(true, 80, 443)
        commentAdapter = CommentAdapter(applicationContext, commentList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/grouppostcomments/$id"
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
                        no.visibility = View.VISIBLE

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
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //val myLdt = LocalDateTime.of(year, month, day, ... )
        val adate = day.toString()+"/"+month.toString()+"/"+year.toString()

        val prefsx = getSharedPreferences("db", Context.MODE_PRIVATE)
        val dp = prefsx.getString("dp", "")
        //comment on the post
        val thecomment = findViewById<EditText>(R.id.addsgrcpostcomment)
        val send = findViewById<ImageButton>(R.id.sendaddsgrcpostcomment)
        send.setOnClickListener {
            val client = AsyncHttpClient(true, 80, 443)
            val data = JSONObject()
            data.put("id", id)
            data.put("email", email)
            data.put("username", username2)
            data.put("comment", thecomment.text)
            data.put("dp", dp)
            data.put("date", adate)
            val condata = StringEntity(data.toString())

            //post it to api
            client.post(this, "http://coding.co.ke/jereson/addgroupcomment",
                condata, "application/json",
                object: JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {

                        Toast.makeText(applicationContext, "comment added ", Toast.LENGTH_LONG).show()
                        thecomment.text.clear()
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

        }//ends send on click listener



    }//ends oncreate
}//ends class