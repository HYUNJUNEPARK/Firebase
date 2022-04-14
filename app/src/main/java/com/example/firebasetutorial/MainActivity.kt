package com.example.firebasetutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasetutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.authMainTextView.text = "${MyApplication.email} 로그인"
        initLogOut()
    }

    private fun initLogOut() {
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email=null
            finish()
        }
    }
}