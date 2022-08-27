package com.example.hansimcat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hansimcat.databinding.ActivityCommentBinding
import com.example.hansimcat.databinding.ActivityMainBinding
import com.example.hansimcat.databinding.ActivityUploadBinding

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}