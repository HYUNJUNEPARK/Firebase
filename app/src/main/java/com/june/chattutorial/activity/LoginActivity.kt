package com.june.chattutorial.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.june.chattutorial.databinding.ActivityLoginBinding
import com.june.chattutorial.firebase.FBVal.Companion.auth
import com.june.chattutorial.firebase.FBVal.Companion.currentUser
import com.june.chattutorial.firebase.FBVal.Companion.initCurrentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val userA_ID = "testa@test.com"
    private val userA_PW = "111111"
    private val userB_ID = "testb@test.com"
    private val userB_PW = "111111"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initUserButtonA(this)
        initUserButtonB(this)
    }

    override fun onResume() {
        super.onResume()
        binding.userAButton.isEnabled = true
        binding.userBButton.isEnabled = true
        currentUser = null
    }

    private fun initUserButtonA(activityContext: LoginActivity) {
        binding.userAButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.userAButton.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                auth.signInWithEmailAndPassword(userA_ID, userA_PW)
                    .addOnCompleteListener { task ->
                        binding.progressBar.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            initCurrentUser()
                            val intent = Intent(activityContext, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(activityContext, "로그인 에러", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun initUserButtonB(activityContext: LoginActivity) {
        binding.userBButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.userBButton.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                auth.signInWithEmailAndPassword(userB_ID, userB_PW)
                    .addOnCompleteListener { task ->
                        binding.progressBar.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            initCurrentUser()
                            val intent = Intent(activityContext, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(activityContext, "로그인 에러", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}