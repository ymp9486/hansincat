package com.example.hansimcat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hansimcat.databinding.ActivitySearchBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.security.Key

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var KeywordList: ArrayList<String>? = getKeywords("keywords")

        binding.searchRvKeyword.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchRvKeyword.adapter = KeywordList?.let { KeywordAdapter(this, it) }

        binding.searchEtKeyword.setOnKeyListener{ v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                KeywordList!!.add(binding.searchEtKeyword.text.toString())
                saveKeywords("keywords", KeywordList)
//                binding.searchRvKeyword.adapter!!.notifyDataSetChanged()
//                binding.searchEtKeyword.text = null
                Intent(this, MainActivity::class.java).apply {
                    putExtra("keyword", binding.searchEtKeyword.text.toString())
                    putExtra("tabFragment", "search")
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
            true
        }
        binding.serchIvBack.setOnClickListener{
            onBackPressed()
        }
    }

    private fun getKeywords(Key: String): ArrayList<String> {
        val prefs = getSharedPreferences("YM", Context.MODE_PRIVATE)
        val json = prefs.getString(Key, "[]")
        val gson = Gson()

        return gson.fromJson(
            json,
            object : TypeToken<ArrayList<String?>>() {}.type
        )
    }

    private fun saveKeywords(key:String, values: ArrayList<String>) {
        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = getSharedPreferences("YM", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, json)
        editor.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}