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

class courses : AppCompatActivity() {
    lateinit var courseList: ArrayList<CourseModel>
    lateinit var courseAdapter: CourseAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        val back = findViewById<ImageButton>(R.id.courseback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, courseaa::class.java))
        }
        recyclerView = findViewById(R.id.recyclercourses)
        courseList=ArrayList<CourseModel>()

//        courseList.add(CourseModel("1", "Bs in software engineering", "To help learners do this and that", "Offers you a vry good opportunity", "4yrs, (8 semester)", "An aggregate C+ in KCSE", "Will hav a very good outcome"))
//        courseList.add(CourseModel("2", "Bs in Mechanical engineering", "To help learners do this and that", "Offers you a vry good opportunity", "4yrs, (8 semester)", "An aggregate C+ in KCSE", "Will hav a very good outcome"))
//        courseList.add(CourseModel("3", "Bs in computer science", "To help learners do this and that", "Offers you a vry good opportunity", "4yrs, (8 semester)", "An aggregate C+ in KCSE", "Will hav a very good outcome"))
//        courseList.add(CourseModel("4", "Bs in Aeronautical engineering", "To help learners do this and that", "Offers you a vry good opportunity", "4yrs, (8 semester)", "An aggregate C+ in KCSE", "Will hav a very good outcome"))
//
//        courseAdapter = CourseAdapter(applicationContext, courseList)
//        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.setHasFixedSize(true)
//        courseAdapter.setCourseListItems(courseList)
//        recyclerView.adapter = courseAdapter


        val prefs = getSharedPreferences("schoolcoursedb", Context.MODE_PRIVATE)
        val id = prefs.getString("db", "")
        val no = findViewById<TextView>(R.id.coursenonet)
        no.visibility = View.GONE

        //get the courses from the database
        val client = AsyncHttpClient(true, 80, 443)
        courseAdapter = CourseAdapter(applicationContext, courseList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/gettcourses/$id"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<CourseModel>::class.java).toList()
                    if (List.size == 0){
                        no.visibility = View.VISIBLE
                    }else {
                        courseAdapter.setCourseListItems(List)
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

        recyclerView.adapter = courseAdapter



    }//ends oncreate
}//ends class