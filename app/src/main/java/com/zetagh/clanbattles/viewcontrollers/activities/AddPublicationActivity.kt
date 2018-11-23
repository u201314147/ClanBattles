package com.zetagh.clanbattles.viewcontrollers.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.common.internal.ResourceUtils
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.zetagh.clanbattles.R
import com.zetagh.clanbattles.networking.ClanBattlesApi.Companion.urlPostPublication
import kotlinx.android.synthetic.main.content_add_publication.*
import kotlinx.android.synthetic.main.activity_add_publication.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddPublicationActivity : AppCompatActivity() {

    private var downloadUri :String?=null
    private var filePath: Uri?=null
    private var bitmap: Bitmap?=null
    private var storage : FirebaseStorage?=null
    private var storageReference: StorageReference?=null
    private var dataByte:ByteArray?=null
    private var mCurrentPhotoPath:String?=null
    private var photoFile: File?=null
    private var id:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_publication)
        setSupportActionBar(toolbar)

        val intentExtras = intent
        id = intentExtras.getIntExtra("id",1)

        //Get instance of firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        buttonListenerToGallery()

        //Load image to firebase
        addPublicationButtonOnClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST &&  resultCode == RESULT_OK && data!= null && data.data !=null){
            Log.d("photo","Entro a ")
            filePath = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                val stream = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG,25,stream)
                dataByte= stream.toByteArray()
                loadFromGalleryButton.setImageBitmap(bitmap)
                loadFromGalleryButton.visibility = View.VISIBLE
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
//        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(mCurrentPhotoPath))
//                loadFromGalleryButton.setImageBitmap(bitmap)
//                loadFromGalleryButton.visibility = View.VISIBLE
//            }catch (e: IOException){
//                Log.d("photo",e.printStackTrace().toString())
//            }
//        }
    }

    private fun buttonListenerToGallery() {
        chooseImage.setOnClickListener{
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i,"SELECT PICTURE"),PICK_IMAGE_REQUEST)
        }
    }

    private fun initPublication(): JSONObject {

        val jsonObject = JSONObject()
        jsonObject.put("title",titleEditView.text)
        jsonObject.put("description",descriptionEditText.text)
        jsonObject.put("urlToImage",downloadUri)
        jsonObject.put("publicationDate","2018-09-29T16:04:20.4070326-07:00")
        jsonObject.put("gameId",id)

        return jsonObject
    }

    private fun uploadImage(){
        if(filePath!=null){

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val ref = storageReference!!.child("images/"+ UUID.randomUUID().toString())
            val uploadTask = ref.putBytes(dataByte!!)
            uploadTask
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                    }
                    .addOnFailureListener { exception ->
                        Log.d("UploadImage",exception.message)
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred/taskSnapshot.totalByteCount
                        progressDialog.setMessage("Uploaded "+ progress.toInt() +"%...")
                    }
                    .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { it ->
                        if(!it.isSuccessful){
                            it.exception?.let {
                                throw it
                            }
                        }
                        return@Continuation ref.downloadUrl
                    }).addOnCompleteListener{
                        if(it.isSuccessful){
                            downloadUri = it.result.toString()
                            //Post a publication
                            postPublication()
                            backToActivity()
                        }
                    }
        }
    }

    private fun addPublicationButtonOnClick(){
        addPublicationButton.setOnClickListener {
            uploadImage()
        }
    }


    private fun postPublication(gamerid:Int = 2){

        val json = initPublication()
        Log.d("postpublication","Este es el json ${json}")
        Log.d("postpublication","URL de descarga ${downloadUri}")

        AndroidNetworking.post(urlPostPublication(gamerid))
                .addJSONObjectBody(json)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("postPublication","On response exitoso -> ${response.toString()}")
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("postPublication","Error failure -> ${anError.toString()}")
                    }

                })
    }
    private fun backToActivity(){
        val context = applicationContext
        context.startActivity(
                Intent(context,MainActivity::class.java)
        )
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1234
//        private const val CAMERA_REQUEST_CODE = 1
    }
}
