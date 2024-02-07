package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.nio.file.Paths.get
import kotlin.collections.ArrayList

class Splashactivity : AppCompatActivity() {
    var coun = 0
    private var tz: ArrayList<String?>? = null
    lateinit var textView: TextView
    lateinit var textView1: TextView
    lateinit var relativeLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashactivity)
            tz = ArrayList()


        textView = findViewById(R.id.campus)
        textView1 = findViewById(R.id.life)
        relativeLayout = findViewById(R.id.relativeLayout)

            val slide = Slide()
            slide.slideEdge = Gravity.START
            TransitionManager.beginDelayedTransition(relativeLayout, slide)
            textView.visibility = View.VISIBLE

            val slide2 = Slide()
            slide2.slideEdge = Gravity.END
            TransitionManager.beginDelayedTransition(relativeLayout, slide2)
            textView1.visibility = View.VISIBLE


        //we delay layout for x seconds
        Handler().postDelayed(  Runnable {          //runnable makes the odes inside runnable
            //during the x seconds delay  we can do various tasks i.e loading GPS or loading something from the database
            //in this case we just don't do anything, we only link to main game
            val myPrefs = getSharedPreferences("db", Context.MODE_PRIVATE)
            val username = myPrefs.getString("username", null)
            val password = myPrefs.getString("password", null)
            if (username != null && password != null )
            {
                //username and password are present, do your stuff
//                funa()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                println("myname"+ username)
            }else
            {
                startActivity(Intent(applicationContext, signup::class.java))
            }
            finish()    //finish this activity completely,,,you can't go back
        }, 4000)




    }//ends oncreate
}//ends class