package com.example.hansimcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hansimcat.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {

            var gojoin = true

            var email = binding.joinemail.text.toString()
            var password1 = binding.joinpassword1.text.toString()
            var password2 = binding.joinpassword2.text.toString()

            //값이 비어 있는지 확인
            if(email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
                gojoin = false
            }

            if(password1.isEmpty()){
                Toast.makeText(this, "패스워드1을 입력해주세요", Toast.LENGTH_LONG).show()
                gojoin = false
            }

            if(password2.isEmpty()){
                Toast.makeText(this, "패스워드2을 입력해주세요", Toast.LENGTH_LONG).show()
                gojoin = false
            }
            
            // 비밀번호 일치여부 확인
            if(!password1.equals(password2)){
                Toast.makeText(this, "패스워드가 같지 않습니다.", Toast.LENGTH_LONG).show()
                gojoin = false
            }

            //비밀번호 길이 혹인
            if(password1.length < 6) {
                Toast.makeText(this, "패스워드가 6자리 이상으로 입력해주세요.", Toast.LENGTH_LONG).show()
                gojoin = false
            }
            if (gojoin) {
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()

                            val intent = Intent(this,MainActivity::class.java)

                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)

                        } else {

                            Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()

                        }
                    }
            }
        }
    }
}