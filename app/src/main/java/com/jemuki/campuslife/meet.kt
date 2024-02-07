package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class meet : AppCompatActivity() {
    lateinit var meetList: ArrayList<MeetModel>
    lateinit var meetAdapter: MeetAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meet)

        val back = findViewById<ImageButton>(R.id.meettoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, meetshow::class.java))
        }

        val add = findViewById<FloatingActionButton>(R.id.toaddpost)
        add.setOnClickListener{
            startActivity(Intent(applicationContext, addpost::class.java))
        }


        recyclerView = findViewById(R.id.recyclermeet)
        meetList=ArrayList<MeetModel>()

//        meetList.add(MeetModel("1",R.drawable.tesla,"This is the first news posted by zetech university.Its a very good piece news", "142"))
//        meetList.add(MeetModel("1",R.drawable.tesla,"This is the first news posted by zetech university.Its a very good piece news", "142"))
//        meetList.add(MeetModel("1",R.drawable.tesla,"This is the first news posted by zetech university.Its a very good piece news", "142"))
//        meetList.add(MeetModel("1",R.drawable.tesla,"This is the first news posted by zetech university.Its a very good piece news", "142"))
//        meetList.add(MeetModel("1",R.drawable.tesla,"This is the first news posted by zetech university.Its a very good piece news", "142"))
//
//        meetAdapter = MeetAdapter(applicationContext, meetList)
//        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.setHasFixedSize(true)
//        meetAdapter.setMeetListItems(meetList)
//        recyclerView.adapter = meetAdapter

        val prefs = getSharedPreferences("mshowdb", Context.MODE_PRIVATE)
        val id = prefs.getString("id", "")


        //get posts
        val client = AsyncHttpClient(true, 80, 443)
        meetAdapter = MeetAdapter(applicationContext, meetList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/getmeets/$id"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<MeetModel>::class.java).toList()
                    if (List.size == 0){
                        Toast.makeText(applicationContext, "No posts yet", Toast.LENGTH_LONG).show()
                         }else {
                        meetAdapter.setMeetListItems(List)
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

        recyclerView.adapter = meetAdapter




    }//ends oncreate
}//ends class