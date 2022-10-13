package com.example.hansimcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.hansimcat.databinding.ActivityMainBinding
import com.example.hansimcat.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if (intent.getStringExtra("tabFragment").equals("search")){
            val searchFragment = SearchFragment()
            val bundle = Bundle()
            bundle.putString("keyword", intent.getStringExtra("keyword"))
            searchFragment.arguments = bundle
            val transition = supportFragmentManager.beginTransaction()
            transition.add(R.id.main_fl, searchFragment)
            transition.commit()
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.main_fl, HomeFragment()).commitAllowingStateLoss()
        }

        binding.mainBottomNav.itemIconTintList = null
        binding.mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, HomeFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, SearchFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_upload -> {
                    val intent = Intent(this, UploadActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.tab_heart -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, HeartFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_mypage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, MyPageFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    false
                }
            }
        }
//        findViewById<Button>(R.id.logout_btn).setOnClickListener {
//            auth.signOut()
//            val intent = Intent(this,IntroActivity::class.java)
//
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
    }
}