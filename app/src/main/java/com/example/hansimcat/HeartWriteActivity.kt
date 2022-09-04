package com.example.hansimcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hansimcat.databinding.ActivityHeartWriteBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

val database = Firebase.database
val boardRef = database.getReference("board")

class HeartWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHeartWriteBinding

    private fun getTime(): String {

        val currentDataTime = Calendar.getInstance().time

        return SimpleDateFormat("yyyy.MM.dd. HH:mm:ss", Locale.KOREA).format(currentDataTime)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_heart_write)

        binding.writeBtn2.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val content = binding.contentsArea.text.toString()
            val time = getTime()

            boardRef
                .push()
                .setValue(board(title,content, "userId", time))

            Toast.makeText(this, "게시물 작성 완료", Toast.LENGTH_LONG).show()

            finish()
        }
    }
}