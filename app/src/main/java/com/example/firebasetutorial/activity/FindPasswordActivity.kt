package com.example.firebasetutorial.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebasetutorial.MyApplication.Companion.TAG
import com.example.firebasetutorial.MyApplication.Companion.auth
import com.example.firebasetutorial.databinding.ActivityFindPasswordBinding
import com.google.firebase.auth.FirebaseUser

class FindPasswordActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFindPasswordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSendChangePasswordEmailButton()
    }

    private fun initSendChangePasswordEmailButton() {
        binding.sendChangePasswordEmailButton.setOnClickListener {
            val userInput = binding.emailTextView.text.toString()
            Log.d(TAG, "onCreate: $userInput")

            auth.sendPasswordResetEmail(userInput)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "비밀번호 재설정 이메일 전송 완료 : ", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(this, "이메일 전송 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}