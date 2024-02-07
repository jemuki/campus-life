package com.jemuki.campuslife

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject
import java.io.IOException
import java.util.*

class addgroup : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var imagePreview: ShapeableImageView
    lateinit var btn_choose_image: ImageButton
    lateinit var btn_upload_image: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addgroup)

        val back  = findViewById<ImageButton>(R.id.addgroupback)
        back.setOnClickListener {
            finish()
        }

        btn_choose_image = findViewById(R.id.chooseicon)
        btn_upload_image = findViewById(R.id.addit)
        imagePreview = findViewById(R.id.iconpreview)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        btn_choose_image.setOnClickListener { launchGallery() }
        btn_upload_image.setOnClickListener { uploadImage() }


    }//ends oncreate


    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }//ends launch gallery

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagePreview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }//ends onactivity result

    private fun uploadImage(){
        if(filePath != null){
            Toast.makeText(applicationContext, "uploading...", Toast.LENGTH_LONG).show()
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
                ?.addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            println("myurl is:"+imageUrl)
                            uploaddetails(imageUrl)
                            Toast.makeText(applicationContext, "Image uploaded successfully", Toast.LENGTH_LONG).show()
                        }
                    })
                ?.addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(applicationContext, "please try again", Toast.LENGTH_LONG).show()
                })


        }else{
            Toast.makeText(this, "Please choose an icon", Toast.LENGTH_SHORT).show()
        }
    }//ends upload image

    private fun uploaddetails(imageUrl: String) {
    val prefs = getSharedPreferences("db", Context.MODE_PRIVATE)
    val email = prefs.getString("email", "")
    val username = prefs.getString("username", "")
    val gname = findViewById<EditText>(R.id.typegname)
    val gdesc = findViewById<EditText>(R.id.typegdesc)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //val myLdt = LocalDateTime.of(year, month, day, ... )
        val adate = day.toString()+"/"+month.toString()+"/"+year.toString()
    val namep = findViewById<TextView>(R.id.groupnamepreview)
    val descp = findViewById<TextView>(R.id.groupdescprebiew)

    val prefs3 = getSharedPreferences("gshowdb", Context.MODE_PRIVATE)
    val id = prefs3.getString("id", "")

    val client = AsyncHttpClient(true, 80, 443)
        val data = JSONObject()
        data.put("email", email)
        data.put("username", username)
        data.put("name", gname.text)
        data.put("description", gdesc.text)
        data.put("icon", imageUrl)
        data.put("id", id)
        data.put("date", adate)

        val condata = StringEntity(data.toString())
        client.post(this, "http://coding.co.ke/jereson/addgroup"
            , condata, "application/json", object :JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "group created successfully", Toast.LENGTH_LONG).show()
                    gname.text.clear()
                    gdesc.text.clear()
                    namep.text = gname.text
                    descp.text = gdesc.text
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(applicationContext, "somethimg went wrong "+statusCode, Toast.LENGTH_LONG).show()
                }
            }
        )//ends client.post

    }//ends upload details function


}//ends class