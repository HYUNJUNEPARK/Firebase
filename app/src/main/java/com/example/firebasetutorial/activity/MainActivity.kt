package com.example.firebasetutorial.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.authMainTextView.text = "${MyApplication.email} 로그인"
        initLogOutButton()
        initChangePasswordButton()
    }

    private fun initLogOutButton() {
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email =null
            finish()
        }
    }

    private fun initChangePasswordButton() {
        binding.changePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}