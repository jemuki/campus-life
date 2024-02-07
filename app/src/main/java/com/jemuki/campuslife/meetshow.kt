package com.jemuki.campuslife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

class meetshow : AppCompatActivity() {
    lateinit var mshowList: ArrayList<MeetshowModel>
    lateinit var meetshowAdapter: MeetshowAdapter
    lateinit var recyclerView: RecyclerView
    // Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meetshow)

        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayoutmshow)

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
                R.id.drawerLayoutmshow -> {
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


        recyclerView = findViewById(R.id.recyclermshow)
        mshowList=ArrayList<MeetshowModel>()

//        mshowList.add(MeetshowModel("1",R.drawable.tesla,"Zetech university", "Invent your future"))
//        mshowList.add(MeetshowModel("1",R.drawable.tesla,"Zetech university", "Invent your future"))
//        mshowList.add(MeetshowModel("1",R.drawable.tesla,"Zetech university", "Invent your future"))
//        mshowList.add(MeetshowModel("1",R.drawable.tesla,"Zetech university", "Invent your future"))
//
//        meetshowAdapter = MeetshowAdapter(applicationContext, mshowList)
//        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.setHasFixedSize(true)
//        meetshowAdapter.setMshowListItems(mshowList)
//        recyclerView.adapter = meetshowAdapter


//        Get the school available
        val client = AsyncHttpClient(true, 80, 443)
        meetshowAdapter = MeetshowAdapter(applicationContext, mshowList)
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
                        Array<MeetshowModel>::class.java).toList()
                    if (List.size == 0){
                        Toast.makeText(applicationContext, "No schools yet", Toast.LENGTH_LONG).show()
                    }else {
                        meetshowAdapter.setMshowListItems(List)

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

        recyclerView.adapter = meetshowAdapter





    }//ends oncraete

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