package com.example.hansimcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hansimcat.databinding.ActivityBoardinsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardinsideActivity : AppCompatActivity() {

    private val TAG = BoardinsideActivity::class.java.simpleName

    private lateinit var binding: ActivityBoardinsideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boardinside)

//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea3.text = title
//        binding.textArea.text = content
//        binding.timeArea2.text = time

        val key = intent.getStringExtra("key")
        getBoardData(key.toString())
        getImageData(key.toString())
    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{

            }
        })

    }
    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(board::class.java)
                binding.titleArea3.text = dataModel!!.title
                binding.textArea.text = dataModel!!.content
                binding.timeArea2.text = dataModel!!.time
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        boardRef.child(key).addValueEventListener(postListener)

    }

}