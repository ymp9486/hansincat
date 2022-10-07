package com.example.hansimcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hansimcat.databinding.ActivityBoardinsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class BoardinsideActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    fun getUid() :String{
        auth = FirebaseAuth.getInstance()

        return auth.currentUser?.uid.toString()
    }

    private val commentRef = database.getReference("comment")

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdepter : CommentLVAdapter

    private val TAG = BoardinsideActivity::class.java.simpleName

    private lateinit var binding: ActivityBoardinsideBinding

    private lateinit var key : String

    private fun getTime(): String {

        val currentDataTime = Calendar.getInstance().time

        return SimpleDateFormat("yyyy.MM.dd. HH:mm:ss", Locale.KOREA).format(currentDataTime)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boardinside)

        binding.Boardsetting.setOnClickListener {
            showDialog()
        }

//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea3.text = title
//        binding.textArea.text = content
//        binding.timeArea2.text = time

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.commentArea1Btn.setOnClickListener {
            insertComment(key!!)
        }

        commentAdepter = CommentLVAdapter(commentDataList)
        binding.commentLV1.adapter = commentAdepter


        getCommentData(key!!)
    }

    fun getCommentData(key: String){

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
        commentRef.child(key).addValueEventListener(postListener)
    }

    fun insertComment(key : String){

        commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(binding.commentArea1.text.toString(),getTime())
            )
        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_LONG)
        binding.commentArea1.setText("")
    }

    private  fun showDialog(){
       val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBulder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시물 수정/삭제")

        val alertDialog = mBulder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정버튼을 눌렀습니다.", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.deletBtn)?.setOnClickListener {
            boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()
        }
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
                binding.getImageArea.isVisible = false
            }
        })

    }
    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {

                    val dataModel = dataSnapshot.getValue(board::class.java)
                    Log.d(TAG, dataModel!!.title)
                    binding.titleArea3.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea2.text = dataModel!!.time
                    val myUid = getUid()
                    val writteruid = dataModel.userId

                    if(myUid.equals(writteruid)){
                        binding.Boardsetting.isVisible = true
                    }else{

                    }

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