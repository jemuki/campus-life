package com.jemuki.campuslife

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import kotlinx.android.synthetic.main.activity_postingroup.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class postingroup : AppCompatActivity() {
    //store uris of picked images
    private var images: ArrayList<Uri?>? = null
    var pimg: ArrayList<String> = ArrayList<String>()
    //current position/index of selected images
    private var position = 0
    private lateinit var send: FloatingActionButton
    //request code to pick image(s)
    private val PICK_IMAGES_CODE = 0

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null
    private var numimgs: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postingroup)

        val back = findViewById<ImageButton>(R.id.postinback)
        back.setOnClickListener {
            finish()
        }

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        //init list
        images = ArrayList()
        pimg = ArrayList()
        send = findViewById(R.id.sendpostnowx)
        send.setOnClickListener {
            Toast.makeText(applicationContext, "button clicked", Toast.LENGTH_LONG).show()
                if (images!!.size >= 1 ){
                    for (imageUri in images!!){
                        if (imageUri != null) {
                            Toast.makeText(applicationContext, "called upload all", Toast.LENGTH_LONG).show()
                            uploadImage(imageUri)
                        }else {
                            uploadsome()
                            Toast.makeText(applicationContext, "called upload some", Toast.LENGTH_LONG).show()
                        }
                    }//ends for
                }//ends if size
                else {
                    Toast.makeText(applicationContext, "called upload some, ,,,", Toast.LENGTH_LONG).show()
                    uploadsome()
                }//ends else for if
        }//ends send on click

        //setup image switcher
        imageSwitcher.setFactory { ImageView(applicationContext) }

        //pick images clicking this button
        pickImagesBtn.setOnClickListener {
            pickImagesIntent()
        }

        //switch to next image clicking this button
        nextBtn.setOnClickListener {
            if (position < images!!.size-1){
                position++
                imageSwitcher.setImageURI(images!![position])
            }
            else{
                //no more images
                Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
            }
        }

        //switch to previous image clicking this button
        previousBtn.setOnClickListener {
            if (position > 0){
                position--
                imageSwitcher.setImageURI(images!![position])
            }
            else{
                //no more images
                Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun pickImagesIntent(){
        val  intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_CODE){

            if (resultCode == Activity.RESULT_OK){

                if (data!!.clipData != null){
                    //picked multiple images
                    //get number of picked images
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count){
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        //add image to list
                        images!!.add(imageUri)
                    }//ends for
                    numimgs = count
                    println("my nuings"+numimgs)
                    //set first image from list to image switcher
                    imageSwitcher.setImageURI(images!![0])
                    position = 0;
                }
                else{
                    //picked single image
                    val imageUri = data.data
                    //set image to image switcher
                    imageSwitcher.setImageURI(imageUri)
                    position = 0;

                }//

            }

        }




    }//ends oncreate


    private fun uploadImage(imageUri: Uri) {
        if(imageUri != null){
            println("uri2"+imageUri)
            Toast.makeText(applicationContext, "uploading...", Toast.LENGTH_LONG).show()
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(imageUri!!)
                ?.addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            println("myurl is:"+imageUrl)
                            pimg!!.add(imageUrl)
                            println("my pimage is"+pimg)
                            when {
                                pimg.size == numimgs ->{
                                    println("my pimgsze" + pimg.size)
                                    uploadall(pimg)
                                   }
                            }//ends when

                            }
                    })
                ?.addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(applicationContext, "please try again", Toast.LENGTH_LONG).show()
                })


        }else {
            //Toast.makeText(applicationContext, "choose an image", Toast.LENGTH_LONG).show()
            uploadsome()
        }
    }//ends upload image



    private fun uploadall(pimg: ArrayList<String>) {
        val prefs2 = getSharedPreferences("groupdb", Context.MODE_PRIVATE)
        val id = prefs2.getString("id", "")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //val myLdt = LocalDateTime.of(year, month, day, ... )
     val date = day.toString()+"/"+month.toString()+"/"+year.toString()

        val prefs = getSharedPreferences("db", Context.MODE_PRIVATE)
        val email = prefs.getString("email", "")
        val name = prefs.getString("username", "")
        val thepost = findViewById<EditText>(R.id.typegrouppost)

        val client = AsyncHttpClient(true, 80, 443)
        //prepare what you will post
        val data = JSONObject()
        //get username from edit text
        data.put("email", email)
        data.put("name", name)
        data.put("post", thepost.text)
        data.put("group_id", id)
        if (pimg.size >= 1){
            data.put("image1", this.pimg.get(0))
        }else {
            data.put("image1", "blank")
        }

        if (pimg.size >= 2){
            data.put("image2", this.pimg.get(1))
        }else {
            data.put("image2", "blank")
        }

        if (pimg.size >= 3){
            data.put("image3", this.pimg.get(2))
        }else {
            data.put("image3", "blank")
        }

        if (pimg.size >= 4){
            data.put("image4", this.pimg.get(3))
        }else {
            data.put("image4", "blank")
        }

        if (pimg.size >= 5){
            data.put("image5", this.pimg.get(4))
        }else {
            data.put("image5", "blank")
        }

        if (pimg.size >= 6){
            data.put("image6", this.pimg.get(5))
        }else {
            data.put("image6", "blank")
        }

        if (pimg.size >= 7){
            data.put("image7", this.pimg.get(6))
        }else {
            data.put("image7", "blank")
        }

        if (pimg.size >= 8){
            data.put("image8", this.pimg.get(7))
        }else {
            data.put("image8", "blank")
        }

        if (pimg.size >= 9){
            data.put("image9", this.pimg.get(8))
        }else {
            data.put("image9", "blank")
        }

        if (pimg.size >= 10){
            data.put("image10", this.pimg.get(9))
        }else {
            data.put("image10", "blank")
        }

         data.put("date", date)

        val condata = StringEntity(data.toString())

        //post it to api
        client.post(this, "http://coding.co.ke/jereson/pingroup2",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "post added successfully", Toast.LENGTH_LONG).show()
                    thepost.text.clear()
//                    images!!.clear()
//                    pimg.clear()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(applicationContext, "Something went wrong "+statusCode, Toast.LENGTH_LONG).show()
                }


            })//ends the client.post

    }//ends upload all function


    //when no image is selected
    private fun uploadsome(){
        Toast.makeText(applicationContext, "uploading...", Toast.LENGTH_LONG).show()
        val prefs2 = getSharedPreferences("groupdb", Context.MODE_PRIVATE)
        val id = prefs2.getString("id", "")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val date = day.toString()+"/"+month.toString()+"/"+year.toString()

        val prefs = getSharedPreferences("db", Context.MODE_PRIVATE)
        val email = prefs.getString("email", "")
        val name = prefs.getString("username", "")
        val thepost = findViewById<EditText>(R.id.typegrouppost)

        val client = AsyncHttpClient(true, 80, 443)
        //prepare what you will post
        val data = JSONObject()
        //get username from edit text
        data.put("email", email)
        data.put("name", name)
        data.put("post", thepost.text)
        data.put("group_id", id)
        data.put("date", date)

        val condata = StringEntity(data.toString())

        //post it to api
        client.post(this, "http://coding.co.ke/jereson/pingroup",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "post added successfully", Toast.LENGTH_LONG).show()
                    thepost.text.clear()
                    images!!.clear()
                    pimg.clear()

                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(applicationContext, "Something went wrong "+statusCode, Toast.LENGTH_LONG).show()
                }


            })//ends the client.post

    }//ends upload some






}//ends class