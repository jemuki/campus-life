package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class hostels : AppCompatActivity() {
    lateinit var hostelList: ArrayList<ResModel>
    lateinit var resAdapter: ResAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hostels)
            val back = findViewById<ImageButton>(R.id.restoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, resshow::class.java))
        }


        recyclerView = findViewById(R.id.recyclerhostels)
        hostelList=ArrayList<ResModel>()

//        hostelList.add(ResModel("1",R.drawable.tesla,"Kiburi Traders hostels", "Ruiru opp Total station"))
//        hostelList.add(ResModel("1",R.drawable.tesla,"Kiburi Traders hostels", "Ruiru opp Total station"))
//        hostelList.add(ResModel("1",R.drawable.tesla,"Kiburi Traders hostels", "Ruiru opp Total station"))
//        hostelList.add(ResModel("1",R.drawable.tesla,"Kiburi Traders hostels", "Ruiru opp Total station"))
//
//        resAdapter = ResAdapter(applicationContext, hostelList)
//        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.setHasFixedSize(true)
//        resAdapter.setHostelListItems(hostelList)
//        recyclerView.adapter = resAdapter

        val prefs = getSharedPreferences("rshowdb", Context.MODE_PRIVATE)
        val id = prefs.getString("id", "")

//        Get the hostels
        val no = findViewById<TextView>(R.id.nohos)
        no.visibility = View.GONE
        val client = AsyncHttpClient(true, 80, 443)
        resAdapter = ResAdapter(applicationContext, hostelList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/hostels/$id"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<ResModel>::class.java).toList()
                    if (List.size == 0){
                        no.visibility = View.VISIBLE

                    }else {
                        resAdapter.setHostelListItems(List)

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

        recyclerView.adapter = resAdapter



    }//ends oncreate
}//ends class