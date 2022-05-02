package com.june.chattutorial.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.june.chattutorial.firebase.FBVal.Companion.auth
import com.june.chattutorial.databinding.ActivityMainBinding
import com.june.chattutorial.firebase.FBVal.Companion.firebaseDBReference
import com.june.chattutorial.model.ChatItemModel


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatDB = firebaseDBReference

        initUserIdTextView()

        binding.sendButton.setOnClickListener {
            val chatItem = ChatItemModel(
                senderId = auth.currentUser!!.uid,
                message = binding.messageEditText.text.toString(),
                sendTime = System.currentTimeMillis()
            )
        }
    }

    private fun initUserIdTextView() {
        binding.userIdTextView.text = auth.currentUser?.email.toString()
    }


}