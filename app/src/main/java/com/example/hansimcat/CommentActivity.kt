package com.example.hansimcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hansimcat.databinding.ActivityCommentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CommentActivity : AppCompatActivity() {

    val database = Firebase.database

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdepter : CommentLVAdapter

    private lateinit var binding: ActivityCommentBinding

    private fun getTime(): String {

        val currentDataTime = Calendar.getInstance().time

        return SimpleDateFormat("yyyy.MM.dd. HH:mm:ss", Locale.KOREA).format(currentDataTime)
    }

    private val commentRef = database.getReference("comment2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val key = intent.getStringExtra("key")
//        Toast.makeText(this, key, Toast.LENGTH_LONG).show()

        binding.commentArea2Btn.setOnClickListener {
            insertComment()
        }
        commentAdepter = CommentLVAdapter(commentDataList)
        binding.commentLV2.adapter = commentAdepter

        getCommentData()
    }

    fun getCommentData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                commentAdepter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        commentRef.addValueEventListener(postListener)

    }
    fun insertComment() {
        commentRef
            .push().setValue(
                CommentModel(binding.commentArea2.text.toString(),getTime())
            )
        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_LONG).show()
        binding.commentArea2.setText("")
    }
}