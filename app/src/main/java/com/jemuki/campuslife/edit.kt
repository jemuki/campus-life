package com.jemuki.campuslife

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
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
import kotlinx.android.synthetic.main.activity_edit.*
class edit : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var dp: String? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var imagePreview: ImageView
    lateinit var btn_choose_image: ImageButton
    lateinit var btn_upload_image: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val back =findViewById<ImageButton>(R.id.edittoback)
        back.setOnClickListener {
            startActivity(Intent(applicationContext, profile::class.java))
        }


        btn_choose_image = findViewById(R.id.choosedp)
        btn_upload_image = findViewById(R.id.senddp)
        imagePreview = findViewById(R.id.dppreview2)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        btn_choose_image.setOnClickListener { launchGallery()
        }

        btn_upload_image.setOnClickListener { uploadImage()
        }

            seusername.setOnClickListener { editusername() }
            sebio.setOnClickListener { editbio() }
            semail.setOnClickListener { editemail() }

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
            val prefsdp = applicationContext.getSharedPreferences("dpimage", Context.MODE_PRIVATE)
            val editordp = prefsdp.edit()
            editordp.putString("image",filePath.toString())
            editordp.apply()
            println("file path"+filePath)
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
            val ref = storageReference?.child("dp/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
                ?.addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            println("myurl is:"+imageUrl)
                            dp = imageUrl
                            val prefsx = getSharedPreferences("db",Context.MODE_PRIVATE)
                            val editor = prefsx.edit()
                            editor.putString("dp", imageUrl)
                            editor.apply()
                            uploadall()
                            Toast.makeText(applicationContext, "updating...", Toast.LENGTH_LONG).show()
                        }
                    })
                ?.addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(applicationContext, "please try again", Toast.LENGTH_LONG).show()
                })


        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }//ends upload image


    private fun uploadall() {
        val prefsz = getSharedPreferences("db", Context.MODE_PRIVATE)
        val emailz = prefsz.getString("email", "")
        val client = AsyncHttpClient(true, 80, 443)
        //prepare what you will post
        val data = JSONObject()
        //get username from edit text
        data.put("email", emailz)
        data.put("dp", dp)

        val condata = StringEntity(data.toString())

        //post it to api
        client.post(this, "http://coding.co.ke/jereson/editcampusdp",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "dp edited successfully", Toast.LENGTH_LONG).show()
                    println("dpresponse"+response)
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
    }//ends uploaddp fun



    private fun editusername(){
        val prefsz = getSharedPreferences("db", Context.MODE_PRIVATE)
        val emailz = prefsz.getString("email", "")
        val eun = findViewById<EditText>(R.id.eusername)
        val client = AsyncHttpClient(true, 80, 443)
        //prepare what you will post
        val data = JSONObject()
        //get username from edit text
        data.put("email", emailz)
        data.put("username", eun.text)

        val condata = StringEntity(data.toString())

        //post it to api
        client.post(this, "http://coding.co.ke/jereson/editcampususername",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "username edited successfully", Toast.LENGTH_LONG).show()
                    val prefsuser = applicationContext.getSharedPreferences("db", Context.MODE_PRIVATE)
                    val editoruser = prefsuser.edit()
                    editoruser.putString("username", eun.text.toString())
                    editoruser.apply()

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

    }//ends edit username


    private fun editbio(){
        val prefsz = getSharedPreferences("db", Context.MODE_PRIVATE)
        val emailz = prefsz.getString("email", "")
        val eun = findViewById<EditText>(R.id.ebio)
        val client = AsyncHttpClient(true, 80, 443)
        //prepare what you will post
        val data = JSONObject()
        //get username from edit text
        data.put("email", emailz)
        data.put("bio", eun.text)

        val condata = StringEntity(data.toString())

        //post it to api
        client.post(this, "http://coding.co.ke/jereson/editcampusbio",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "bio edited successfully", Toast.LENGTH_LONG).show()
                    val prefsuser = applicationContext.getSharedPreferences("db", Context.MODE_PRIVATE)
                    val editoruser = prefsuser.edit()
                    editoruser.putString("bio", eun.text.toString())
                    editoruser.apply()

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

    }//ends edit username


    private fun editemail(){
        val prefsz = getSharedPreferences("db", Context.MODE_PRIVATE)
        val emailz = prefsz.getString("email", "")
        val eun = findViewById<EditText>(R.id.eemail)
        val client = AsyncHttpClient(true, 80, 443)
        //prepare what you will post
        val data = JSONObject()
        //get username from edit text
        data.put("email", emailz)
        data.put("new", eun.text)

        val condata = StringEntity(data.toString())


        //post it to api
        client.post(this, "http://coding.co.ke/jereson/editcampusemail",
            condata, "application/json",
            object: JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext, "email edited successfully", Toast.LENGTH_LONG).show()
                    val prefsuser = applicationContext.getSharedPreferences("db", Context.MODE_PRIVATE)
                    val editoruser = prefsuser.edit()
                    editoruser.putString("email", eun.text.toString())
                    editoruser.apply()

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

    }//ends edit username



}//ebds class