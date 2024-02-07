package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val back = findViewById<ImageButton>(R.id.profiletoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        val toedit = findViewById<ImageButton>(R.id.toedituser)
        toedit.setOnClickListener {
            startActivity(Intent(applicationContext, edit::class.java))
        }
        val prefs2 = getSharedPreferences("db", Context.MODE_PRIVATE)
        val name = findViewById<TextView>(R.id.pname)
        name.text = prefs2.getString("username", "")
        val bio = findViewById<TextView>(R.id.pbio)
        bio.text = prefs2.getString("bio" , "")


        val prefs = getSharedPreferences("dpimage", Context.MODE_PRIVATE)
        val thedp = prefs.getString("image", "")
        val preview = findViewById<ShapeableImageView>(R.id.dppreview)
        Picasso.with(applicationContext).load(thedp).into(preview)
        println("mydbimageis"+prefs.getString("image", ""))
    }//ends oncreate

}//ends class