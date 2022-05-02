package com.june.chattutorial.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.june.chattutorial.databinding.ItemPartnerUserChatBinding
import com.june.chattutorial.firebase.FBVal.Companion.TAG
import com.june.chattutorial.firebase.FBVal.Companion.currentUser
import com.june.chattutorial.key.ViewType.Companion.CURRENT_USER_MESSAGE
import com.june.chattutorial.key.ViewType.Companion.PARTNER_USER_MESSAGE
import com.june.chattutorial.model.ChatItemModel

class ChatItemAdapter : ListAdapter<ChatItemModel, ChatItemAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemPartnerUserChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItemModel) {
            binding.messageTextView.text = chatItem.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPartnerUserChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return when (viewType) {
            CURRENT_USER_MESSAGE -> {
                Log.d(TAG, "onCreateViewHolder: 내가 보낸 메세지")
                ViewHolder(binding)
            }
            PARTNER_USER_MESSAGE  -> {
                Log.d(TAG, "onCreateViewHolder: 남이 보낸 메세지")
                ViewHolder(binding)
            }
            else -> {
                Log.d(TAG, "--------------")
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        val message = currentList[position]
        return if (message.senderId == currentUser?.uid) {
            CURRENT_USER_MESSAGE
        } else {
            PARTNER_USER_MESSAGE
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatItemModel>() {
            override fun areItemsTheSame(oldItem: ChatItemModel, newItem: ChatItemModel): Boolean {
                return oldItem.sendTime == newItem.sendTime
            }
            override fun areContentsTheSame(oldItem: ChatItemModel, newItem: ChatItemModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}