package com.example.hansimcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hansimcat.databinding.ActivityBoardEditBinding

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class BoardEditActivity : AppCompatActivity() {

    val boardRef = database.getReference("board")

    private lateinit var key : String

    private lateinit var binding: ActivityBoardEditBinding

    private val TAG = BoardinsideActivity::class.java.simpleName

    private lateinit var wittruid : String

    private fun getTime(): String {

        val currentDataTime = Calendar.getInstance().time

        return SimpleDateFormat("yyyy.MM.dd. HH:mm:ss", Locale.KOREA).format(currentDataTime)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_board_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()

        getImageData(key)
        getBoardData(key)
        binding.editBtn2.setOnClickListener {
            editBorddata(key)
        }
    }

    private fun editBorddata(key: String){

        boardRef
            .child(key)
            .setValue(board(binding.titleArea.text.toString(),
                binding.contentsArea.text.toString(),
                wittruid,
                getTime())
            )
        Toast.makeText(this, "수정완료", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{
                binding.imageArea.isVisible = false
            }
        })

    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {

                    val dataModel = dataSnapshot.getValue(board::class.java)
//                    Log.d(TAG, dataModel!!.title)
                    binding.titleArea.setText(dataModel?.title)
                    binding.contentsArea.setText(dataModel?.content)
//                    binding.titleArea.setText(dataModel.time)
                    wittruid = dataModel!!.userId

                }catch (e : Exception){
                    Log.d(TAG, "삭제완료")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        boardRef.child(key).addValueEventListener(postListener)

    }

}