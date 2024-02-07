package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_singlenews.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class singlenews : AppCompatActivity() {
    lateinit var imageList: ArrayList<ImageModel>
    lateinit var imageAdapter: ImageAdapter
    lateinit var commentList: ArrayList<CommentModel>
    lateinit var commentAdapter: CommentAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlenews)

        imageList=ArrayList<ImageModel>()

        val back = findViewById<ImageButton>(R.id.snewsoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, news::class.java))
        }
        val prefs = getSharedPreferences("newsdb",  Context.MODE_PRIVATE)
        val news = findViewById<TextView>(R.id.tsnews)
        news.text = prefs.getString("news", "")
        val id = prefs.getString("id", "")
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

        recyclerView = findViewById(R.id.recyclernewscomments)
        commentList=ArrayList<CommentModel>()

        val no = findViewById<TextView>(R.id.nonewscomments)
        no.visibility = View.GONE
        val client = AsyncHttpClient(true, 80, 443)
        commentAdapter = CommentAdapter(applicationContext, commentList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/newscomments/$id"
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


        //post the added comment
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //val myLdt = LocalDateTime.of(year, month, day, ... )
        val adate = day.toString()+"/"+month.toString()+"/"+year.toString()

        val prefsx = getSharedPreferences("db", Context.MODE_PRIVATE)
        val email = prefsx.getString("email", "")
        val username = prefsx.getString("username", "")
        val nc = findViewById<EditText>(R.id.addcomment)
        val send = findViewById<ImageButton>(R.id.sendnewscomment)
        send.setOnClickListener {
                val client = AsyncHttpClient(true, 80, 443)
                //prepare what you will post
                val data = JSONObject()
                //get data from edit text
                data.put("email", email)
                data.put("username", username)
                data.put("newsid", id)
                data.put("comment", nc.text.toString())
                data.put("date", adate)



                //convert data into a format that our api is using
                val condata = StringEntity(data.toString())

                //post it to api
                client.post(this, "http://coding.co.ke/jereson/addnewscomment",
                    condata, "application/json",
                    object: JsonHttpResponseHandler() {
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            response: JSONObject
                        ) {
                            Toast.makeText(applicationContext, "Comment added successfully", Toast.LENGTH_LONG).show()
                            nc.text.clear()



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



        }//ends on click listener




    }//ends oncreate
}//ends class