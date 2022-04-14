package com.example.firebasetutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasetutorial.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        signInButton()
    }

    private fun signInButton() {
        binding.signBtn.setOnClickListener {
            val email: String = binding.authEmailEditView.text.toString()
            val password: String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful){
                                    Toast.makeText(baseContext, "회원가입에 성공하였습니다. 전송된 메일을 확인해 주세요.", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else {
                                    Toast.makeText(baseContext, "메일 전송 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}