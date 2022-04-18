package com.example.firebasetutorial.activity_auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.R
import com.example.firebasetutorial.activity_firestore.*
import com.example.firebasetutorial.activity_storage.StorageActivity
import com.example.firebasetutorial.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    lateinit var resultListener: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSignIn()
        initGoogleLogin()
        initLoginButton()
        initFindPasswordButton()
        initGoToStoreActivityButton()
        initFireStorageActivityButton()

        resultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    MyApplication.auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                MyApplication.email =account.email
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                } catch (e: ApiException) {
                    Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initFindPasswordButton() {
        binding.sendChangePasswordEmailButton.setOnClickListener {
            val intent = Intent(this, FindPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSignIn() {
        binding.goSignInBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initGoogleLogin() {
        binding.googleLoginBtn.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            resultListener.launch(signInIntent)
        }
    }

    private fun initLoginButton() {
        binding.loginBtn.setOnClickListener {
            val email: String = binding.authEmailEditView.text.toString()
            val password: String = binding.authPasswordEditView.text.toString()
            
            if (email == "" || password == "") {
                Toast.makeText(this, "ID/PW 를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        if(MyApplication.checkAuth()) {
                            MyApplication.email = email
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this, "전송된 메일로 이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initGoToStoreActivityButton() {
        binding.noMemberActivityButton.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initFireStorageActivityButton() {
        binding.FirebaseStorageButton.setOnClickListener {
            val intent = Intent(this, StorageActivity::class.java)
            startActivity(intent)
        }
    }
}