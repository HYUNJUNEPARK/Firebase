package com.example.firebasetutorial.activity_auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetutorial.MyApplication.Companion.auth
import com.example.firebasetutorial.MyApplication.Companion.email
import com.example.firebasetutorial.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.FirebaseUser

class ChangePasswordActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChangePasswordBinding.inflate(layoutInflater) }
    lateinit var user:FirebaseUser
    lateinit var newPassword:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.userIdTextView.text = email
        user = auth.currentUser!!

        initChangePasswordButton()
    }

    private fun initChangePasswordButton() {
        binding.changePasswordButton.setOnClickListener {
            val userInput = binding.changePasswordTextView.text.toString()
            val checkUserInput = binding.checkPasswordTextView.text.toString()

            if (userInput == checkUserInput && userInput != "" && checkUserInput != "") {
                newPassword = userInput

                user!!.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else {
                            Toast.makeText(this, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}