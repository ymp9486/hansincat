package com.example.hansimcat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.hansimcat.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private var showOneTapUI = true
    val GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient (this, gso)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnGoogleSignIn.setOnClickListener {
            googleLogin()
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            var result= Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result!!.isSuccess){
                var account = result?.signInAccount
                firebaseAuthWithGoogle(account)
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    moveMain(user)
                } else {
                    val loginfailalert = AlertDialog.Builder(this)
                    loginfailalert.setMessage("잠시 후 다시 시도해주세요.")
                    loginfailalert.setPositiveButton("확인",null)
                    loginfailalert.show()
                    moveMain(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        //유저가 로그인되어있는지 확인
        val currentUser = auth.currentUser
        moveMain(currentUser)
    }

    //유저가 로그인하면 메인액티비티로 이동
    private fun moveMain(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    fun googleLogin() {
        var signIntent = googleSignInClient.signInIntent
        startActivityForResult(signIntent, GOOGLE_LOGIN_CODE)
    }
}