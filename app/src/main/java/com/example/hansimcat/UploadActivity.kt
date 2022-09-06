package com.example.hansimcat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PathPermission
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hansimcat.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val PICK_STORAGE = 1001
    private var PICK_CAMERA = 1000
    private var PEMISSIONS_REQUEST = 100
    private var imagrUri: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private lateinit var database: DatabaseReference


    private var Permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseStorage = Firebase.storage
        val db = Firebase.database
        database = db.getReference("FeedList")

        pickImage()
        checkPermissions(Permissions)

        binding.uploadIvImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, PICK_CAMERA)
            }
        }
        binding.uploadBtnBack.setOnClickListener {
            onBackPressed()
        }
        binding.uploadBtnComplete.setOnClickListener {
            upkoadImage()
        }
    }

    private fun pickImage() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/* video/*"

        startActivityForResult(intent, PICK_STORAGE)
    }

    private fun upkoadImage() {
        val timestamp = SimpleDateFormat("yyyymmdd_HHmmss").format(Date())
        val imageFileName = "$timestamp.jpeg"
        val content = binding.uploadEt.text.toString()
        val storageReference = firebaseStorage?.reference?.child(imageFileName)
        storageReference?.putFile(imagrUri!!)?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageReference.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                database.get().addOnSuccessListener { it ->
                    var values = it.value as ArrayList<HashMap<String, Any>>?
                    database.child((values?.size?: 0 + 1).toString()).setValue(Feed("YM",
                        downloadUri.toString(), downloadUri.toString(),0,false,false, content))
                }
                Toast.makeText(this, "게시물 작성 완료", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        var permissionList: MutableList<String> = mutableListOf()
        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission)
            }
        }
        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, permissionList.toTypedArray(), PEMISSIONS_REQUEST
            )
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_STORAGE) {
                val pickedImaage: Uri? = data?.data
                if (pickedImaage != null) {
                    imagrUri = pickedImaage
                }
            }
            Glide.with(this).load(imagrUri).into(binding.uploadIvImage)
        }
        if (requestCode == PICK_CAMERA) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val pickedImage: Uri? = data.data
            if (pickedImage != null) {
                imagrUri = pickedImage
            }
            binding.uploadIvImage.setImageBitmap(imageBitmap)
        }
    }
}
