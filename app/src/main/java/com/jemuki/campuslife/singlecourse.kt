package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import org.w3c.dom.Text

class singlecourse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlecourse)
        val back = findViewById<ImageButton>(R.id.sctoc)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, courses::class.java))
        }

        val prefs = getSharedPreferences("coursedb", Context.MODE_PRIVATE)
        val id = prefs.getString("id", "")
        val image = findViewById<ShapeableImageView>(R.id.coursecover)
        //image.setImageURI(prefs.getString("cover", ""))
        val name = findViewById<TextView>(R.id.cname)
        name.text = prefs.getString("name", "")
        val goal = findViewById<TextView>(R.id.cgoals)
        goal.text = prefs.getString("goal", "")
        val opp = findViewById<TextView>(R.id.copp)
        opp.text = prefs.getString("opportunities", "")
        val duration = findViewById<TextView>(R.id.cduration)
        duration.text = prefs.getString("duration", "")
        val qualification = findViewById<TextView>(R.id.crequirements)
        qualification.text = prefs.getString("qualification", "")
        val outcome = findViewById<TextView>(R.id.coutcome)
        outcome.text = prefs.getString("outcome", "")
    }//ends oncreate
}//ends class