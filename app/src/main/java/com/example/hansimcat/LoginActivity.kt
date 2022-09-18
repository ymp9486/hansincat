package com.example.hansimcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hansimcat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginBtn.setOnClickListener {

            var logingo = true

            var email = binding.loginemail.text.toString()
            var password = binding.loginpassword.text.toString()

            if (email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
                logingo = false
            }

            if (password.isEmpty()){
                Toast.makeText(this, "패스워드을 입력해주세요", Toast.LENGTH_LONG).show()
                logingo = false
            }


            if(logingo){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG)

                            val intent = Intent(this,MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)

                        } else {

                            Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG)

                        }
                    }
            }

        }
    }
}