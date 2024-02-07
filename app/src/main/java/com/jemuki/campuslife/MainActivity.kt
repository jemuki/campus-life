package com.jemuki.campuslife
//!xbVL!4p$5jin,#
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var schoolList: ArrayList<SchoolModel>
    lateinit var schoolAdapter: SchoolAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = findViewById<ImageButton>(R.id.toprofile)
        user.setOnClickListener {
            startActivity(Intent(applicationContext, profile::class.java))
        }
        val x = findViewById<ImageButton>(R.id.toag)
        x.setOnClickListener {
            startActivity(Intent(applicationContext, grouppost::class.java))
        }
        val y = findViewById<ImageButton>(R.id.pig)
        y.setOnClickListener {
            startActivity(Intent(applicationContext, singlemeet::class.java))
        }
        val z = findViewById<ImageButton>(R.id.adpo)
        z.setOnClickListener {
            startActivity(Intent(applicationContext, meet::class.java))
        }
        val zx = findViewById<ImageButton>(R.id.togs)
        zx.setOnClickListener {
            startActivity(Intent(applicationContext, singlegroup::class.java))
        }

        recyclerView = findViewById(R.id.recyclermain)
        schoolList=ArrayList<SchoolModel>()

//        schoolList.add(SchoolModel("1",R.drawable.tesla,"Zetech University","Invent your future"))
//        schoolList.add(SchoolModel("2",R.drawable.tesla,"Mount Kenya University","Unlocking infinite possibilities"))
//        schoolList.add(SchoolModel("3",R.drawable.tesla,"Kenyatta University","Elimu ni mwangaza"))
//        schoolList.add(SchoolModel("4",R.drawable.tesla,"University of Nairobi","Invent your future"))
//        schoolList.add(SchoolModel("5",R.drawable.tesla,"Moi University","Invent your future"))
//
//
//        schoolAdapter = SchoolAdapter(applicationContext, schoolList)
//        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.setHasFixedSize(true)
//        schoolAdapter.setSchoolListItems(schoolList)
//        recyclerView.adapter = schoolAdapter
//
        val pro = findViewById<ProgressBar>(R.id.schoolprogress)
        val client = AsyncHttpClient(true, 80, 443)
        schoolAdapter = SchoolAdapter(applicationContext, schoolList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/getschools"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<SchoolModel>::class.java).toList()
                    if (List.size == 0){
                        pro.visibility = View.GONE
                    }else {
                        schoolAdapter.setSchoolListItems(List)
                        pro.visibility = View.GONE
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

        recyclerView.adapter = schoolAdapter


    }//ends oncreate
}//ends class