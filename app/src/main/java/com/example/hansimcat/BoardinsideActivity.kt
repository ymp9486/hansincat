package com.example.hansimcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.hansimcat.databinding.ActivityBoardinsideBinding
class BoardinsideActivity : AppCompatActivity() {

    private val TAG = BoardinsideActivity::class.java.simpleName

    private lateinit var binding: ActivityBoardinsideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boardinside)

        val title = intent.getStringExtra("title").toString()
        val content = intent.getStringExtra("content").toString()
        val time = intent.getStringExtra("time").toString()

        binding.titleArea3.text = title
        binding.textArea.text = content
        binding.timeArea2.text = time
    }
}