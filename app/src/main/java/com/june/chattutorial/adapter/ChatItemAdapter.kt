package com.june.chattutorial.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.june.chattutorial.databinding.ItemMyChatBinding
import com.june.chattutorial.databinding.ItemPartnerUserChatBinding
import com.june.chattutorial.firebase.FBVal.Companion.currentUser
import com.june.chattutorial.key.ViewType.Companion.CURRENT_USER_MESSAGE
import com.june.chattutorial.key.ViewType.Companion.PARTNER_USER_MESSAGE
import com.june.chattutorial.model.ChatItemModel

class ChatItemAdapter : ListAdapter<ChatItemModel, RecyclerView.ViewHolder>(diffUtil) {
    inner class PartnerUserViewHolder(private val binding: ItemPartnerUserChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItemModel) {
            binding.messageTextView.text = chatItem.message
        }
    }

    inner class MyChatViewHolder(private val binding: ItemMyChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItemModel) {
            binding.messageTextView.text = chatItem.message
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val partnerUserBinding = ItemPartnerUserChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val currentUserBinding = ItemMyChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)

         when (viewType) {
            CURRENT_USER_MESSAGE -> {
                return PartnerUserViewHolder(partnerUserBinding)
            }
            PARTNER_USER_MESSAGE  -> {
                return MyChatViewHolder(currentUserBinding)
            }
            else -> {
                return PartnerUserViewHolder(partnerUserBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //TODO
        if (currentList[position].senderId == currentUser?.uid) {
            val holder: PartnerUserViewHolder = holder as PartnerUserViewHolder
            holder.bind(currentList[position])
        }
        else {
            val holder: MyChatViewHolder = holder as MyChatViewHolder
            holder.bind(currentList[position])
        }

        //멀티 뷰 참고 링크
        //https://youngest-programming.tistory.com/69
        //holder.bind(currentList[position])
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