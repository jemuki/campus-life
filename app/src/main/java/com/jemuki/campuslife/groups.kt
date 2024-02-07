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
import com.jemuki.GroupModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class groups : AppCompatActivity() {
    lateinit var groupList: ArrayList<GroupModel>
    lateinit var groupAdapter: GroupAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        val back = findViewById<ImageButton>(R.id.grouptoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, gshow::class.java))
        }
        val add = findViewById<FloatingActionButton>(R.id.addgroup)
        add.setOnClickListener {
            startActivity(Intent(applicationContext, addgroup::class.java))
        }

        recyclerView = findViewById(R.id.recyclergroups)
        groupList=ArrayList<GroupModel>()

//        groupList.add(GroupModel("1",R.drawable.tesla, "My group one"))
//        groupList.add(GroupModel("2",R.drawable.tesla, "My group two"))
//        groupList.add(GroupModel("3",R.drawable.tesla, "My group three"))
//        groupList.add(GroupModel("4",R.drawable.tesla, "My group four"))
//
//        groupAdapter = GroupAdapter(applicationContext, groupList)
//        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.setHasFixedSize(true)
//        groupAdapter.setGroupListItems(groupList)
//        recyclerView.adapter = groupAdapter

        val prefs = getSharedPreferences("gshowdb", Context.MODE_PRIVATE)
        val id = prefs.getString("id", "")
        val client = AsyncHttpClient(true, 80, 443)
        groupAdapter = GroupAdapter(applicationContext, groupList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/groups/$id"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<GroupModel>::class.java).toList()
                    if (List.size == 0){
                        Toast.makeText(applicationContext, "No group yet", Toast.LENGTH_LONG).show()
                         }else {
                        groupAdapter.setGroupListItems(List)
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

        recyclerView.adapter = groupAdapter



    }//ends oncreate
}//ends class