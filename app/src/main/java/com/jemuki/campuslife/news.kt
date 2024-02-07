package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class news : AppCompatActivity() {

    lateinit var newsList: ArrayList<NewsModel>
    lateinit var newsAdapter: NewsAdapter
    lateinit var recyclerView: RecyclerView
    // Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayoutnews)

        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()


        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))

                    true
                }

                R.id.drnews -> {
                    startActivity(Intent(this, news::class.java))

                    Toast.makeText(this, "swipe left for menu", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.drcourses -> {
                    startActivity(Intent(this, courseaa::class.java))

                    Toast.makeText(this, "swipe left for menu", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.drgroups -> {
                    startActivity(Intent(this, gshow::class.java))

                    Toast.makeText(this, "swipe left for menu", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.drmeet -> {
                    startActivity(Intent(this, meetshow::class.java))

                    Toast.makeText(this, "swipe left for menu", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.drres -> {
                    startActivity(Intent(this, resshow::class.java))

                    Toast.makeText(this, "swipe left for menu", Toast.LENGTH_SHORT).show()
                    true
                }



                else -> {
                    false
                }
            }
        }


        val wh = findViewById<TextView>(R.id.who)
        val prefs = getSharedPreferences("schooldb", Context.MODE_PRIVATE)
        wh.text = prefs.getString("name", "")
        val id = prefs.getString("id", "")

        recyclerView = findViewById(R.id.recyclernews)
        newsList=ArrayList<NewsModel>()

//        newsList.add(NewsModel("1",R.drawable.tesla,R.drawable.tesla,R.drawable.tesla, R.drawable.tesla, R.drawable.tesla, "This is the first news posted by zetech university.Its a very good piece news"))
//        newsList.add(NewsModel("2",R.drawable.tesla,R.drawable.tesla,R.drawable.tesla, R.drawable.tesla, R.drawable.tesla, "This is the first news posted by zetech university.Its a very good news"))
//        newsList.add(NewsModel("3",R.drawable.tesla,R.drawable.tesla,R.drawable.tesla, R.drawable.tesla, R.drawable.tesla, "This is the first news posted by zetech university.Its a very good news"))
//        newsList.add(NewsModel("4",R.drawable.tesla,R.drawable.tesla,R.drawable.tesla, R.drawable.tesla, R.drawable.tesla, "This is the first news posted by zetech university.Its a very good news"))
//
//        newsAdapter = NewsAdapter(applicationContext, newsList)
//        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.setHasFixedSize(true)
//        newsAdapter.setNewsListItems(newsList)
//        recyclerView.adapter = newsAdapter
            val no = findViewById<TextView>(R.id.nonews)
                no.visibility = View.GONE
        val client = AsyncHttpClient(true, 80, 443)
        newsAdapter = NewsAdapter(applicationContext, newsList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "http://coding.co.ke/jereson/news/$id"
            ,null, "application/json",
            object  : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {
                    val gson = GsonBuilder().create()
                    val List = gson.fromJson(response.toString(),
                        Array<NewsModel>::class.java).toList()
                    if (List.size == 0){
                        no.visibility = View.VISIBLE
                        }else {
                        newsAdapter.setNewsListItems(List)
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

        recyclerView.adapter = newsAdapter



    }//ends oncreate

    // override the onSupportNavigateUp() function to launch the Drawer when the hamburger icon is clicked
    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
    }

    // override the onBackPressed() function to close the Drawer when the back button is clicked
    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }//ends on back pressed

}//ends class