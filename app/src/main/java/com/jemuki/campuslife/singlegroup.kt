package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class singlegroup : AppCompatActivity() {
    lateinit var grouppostList: ArrayList<SinglegroupModel>
    lateinit var singlegroupAdapter: SinglegroupAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlegroup)
        val back = findViewById<ImageButton>(R.id.sgrouptoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, groups::class.java))
        }
        val addingroup = findViewById<FloatingActionButton>(R.id.postingroup)
        addingroup.setOnClickListener {
            startActivity(Intent(applicationContext, postingroup::class.java))
        }

        val prefs = getSharedPreferences("groupdb", Context.MODE_PRIVATE)
        val id = prefs.getString("id", "")
        println("my group id"+id)
        val name = prefs.getString("name", "")
        val icon = prefs.getString("icon", "")
        println("mygricon is "+icon)
        val groupname = findViewById<TextView>(R.id.grname)
        groupname.text = name
        val viewimg = findViewById<ShapeableImageView>(R.id.grocover)
        val imageUri = prefs.getString("icon", "")
        Picasso.with(applicationContext).load(imageUri).into(viewimg)

        //get the groups posts
        recyclerView = findViewById(R.id.recyclergroupposts)
        grouppostList=ArrayList<SinglegroupModel>()
        val client = AsyncHttpClient(true, 80, 443)
        singlegroupAdapter = SinglegroupAdapter(applicationContext, grouppostList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/grouppost/$id"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<SinglegroupModel>::class.java).toList()
                    if (List.size == 0){
                        Toast.makeText(applicationContext, "No posts yet", Toast.LENGTH_LONG).show()
                    }else {
                        singlegroupAdapter.setGrouppostListItems(List)
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

        recyclerView.adapter = singlegroupAdapter




    }//ends oncreate
}//ends class