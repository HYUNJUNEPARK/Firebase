package com.june.chattutorial.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.june.chattutorial.R
import com.june.chattutorial.adapter.ChatItemAdapter
import com.june.chattutorial.databinding.ActivityMainBinding
import com.june.chattutorial.firebase.FBVal.Companion.currentUser
import com.june.chattutorial.firebase.FBVal.Companion.firebaseDBReference
import com.june.chattutorial.key.DBKey.Companion.DB_CHAT_TUTORIAL
import com.june.chattutorial.key.DBKey.Companion.MESSAGE
import com.june.chattutorial.key.DBKey.Companion.SENDER_ID
import com.june.chattutorial.key.DBKey.Companion.SEND_TIME
import com.june.chattutorial.key.UserIDPW.Companion.userA_ID
import com.june.chattutorial.key.UserIDPW.Companion.userA_UID
import com.june.chattutorial.key.UserIDPW.Companion.userB_UID
import com.june.chattutorial.model.ChatItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var currentUserDB: DatabaseReference
    private lateinit var partnerUserDB: DatabaseReference
    private val chatList = mutableListOf<ChatItemModel>()
    private val adapter = ChatItemAdapter()
    private lateinit var partnerUid: String
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            chatList.clear()
            if (snapshot.value == null) {
                binding.coverTextView.visibility = View.VISIBLE
                return
            }
            for (chatDialogue in snapshot.children) {
                val chatDialogueMap = chatDialogue.value as HashMap<String, String>
                val senderId = chatDialogueMap[SENDER_ID]
                val message = chatDialogueMap[MESSAGE]
                val sendTime =
                    if (chatDialogueMap[SEND_TIME] == null) "0" else chatDialogueMap[SEND_TIME].toString()

                val chatModel = ChatItemModel(
                    senderId = senderId,
                    message = message,
                    sendTime = sendTime.toLong()
                )
                chatList.add(chatModel)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        partnerUid = if (currentUser!!.uid == userA_UID) userB_UID else userA_UID

        currentUserDB =
            firebaseDBReference
            .child(DB_CHAT_TUTORIAL)
            .child(currentUser!!.uid)

        partnerUserDB =
            firebaseDBReference
            .child(DB_CHAT_TUTORIAL)
            .child(partnerUid)

        initCurrentUserUI()
        initSendButton()
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        currentUserDB
            .removeEventListener(valueEventListener)
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        currentUserDB
            .addValueEventListener(valueEventListener)
    }

    private fun initCurrentUserUI() {
        binding.userIdTextView.text = currentUser!!.email.toString()
        if (currentUser!!.email == userA_ID) {
            binding.userProfileImageView.background =
                ResourcesCompat.getDrawable(resources, R.drawable.user_a, null)
        } else {
            binding.userProfileImageView.background =
                ResourcesCompat.getDrawable(resources, R.drawable.user_b, null)
        }
    }

    private fun initSendButton() {
        binding.sendButton.setOnClickListener {
            val chatItem = ChatItemModel(
                senderId = currentUser!!.uid,
                message = binding.messageEditText.text.toString(),
                sendTime = System.currentTimeMillis()
            )
            CoroutineScope(Dispatchers.IO).launch {
                currentUserDB
                    .push()
                    .setValue(chatItem)
                partnerUserDB
                    .push()
                    .setValue(chatItem)
            }
            binding.messageEditText.text = null
        }
    }
}