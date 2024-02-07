package com.jemuki.campuslife

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.jemuki.ImageModel
import kotlinx.android.synthetic.main.activity_singlehostel.*
import java.util.ArrayList

class singlehostel : AppCompatActivity() {
    lateinit var imageList: ArrayList<ImageModel>
    lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singlehostel)
        val back = findViewById<ImageButton>(R.id.shostoback)
        back.setOnClickListener {
           finish()
        }

        val prefs = getSharedPreferences("hosteldb", Context.MODE_PRIVATE)
        val name = findViewById<TextView>(R.id.hosname2)
        name.text = prefs.getString("name", "")
        val desc = findViewById<TextView>(R.id.hosdesc)
        desc.text = prefs.getString("desc", "")
        val price = findViewById<TextView>(R.id.hosprice2)
        price.text = prefs.getString("price", "")
        val location = findViewById<TextView>(R.id.hosloc2)
        location.text = prefs.getString("location", "")
        val phone = findViewById<TextView>(R.id.hosphone)
        phone.text = prefs.getString("phone", "")

        imageList=ArrayList<ImageModel>()

        imageList.add(ImageModel(prefs.getString("image1", "")))
        imageList.add(ImageModel(prefs.getString("image2", "")))
        imageList.add(ImageModel(prefs.getString("image3", "")))
        imageList.add(ImageModel(prefs.getString("image4", "")))
        imageList.add(ImageModel(prefs.getString("image5", "")))
        imageList.add(ImageModel(prefs.getString("image6", "")))
        imageList.add(ImageModel(prefs.getString("image7", "")))
        imageList.add(ImageModel(prefs.getString("image8", "")))
        imageList.add(ImageModel(prefs.getString("image9", "")))
        imageList.add(ImageModel(prefs.getString("image10", "")))


        imageAdapter = ImageAdapter(applicationContext, imageList)
        imageAdapter.setImageListItems(imageList)
        viewPager.adapter = imageAdapter

        val contact = prefs.getString("phone", "")
        val call =  findViewById<Button>(R.id.hosbtn)
        call.setOnClickListener {
            val call2 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(call2)

        }   //ends call

    }//ends oncreate
}//ends class