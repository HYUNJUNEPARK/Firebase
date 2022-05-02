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
import com.june.chattutorial.model.ChatItemModel

class ChatItemAdapter : ListAdapter<ChatItemModel, ChatItemAdapter.PartnerHolder>(diffUtil) {


    inner class PartnerHolder(private val binding: ItemPartnerUserChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItemModel) {
            binding.messageTextView.text = chatItem.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerHolder {
        val binding = ItemPartnerUserChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartnerHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnerHolder, position: Int) {
        holder.bind(currentList[position])
    }

//    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//
//        val tt: ChatItemModel = getItem(position)
//
//        //내가 보낸 메시지
//        return if (tt.senderId == currentUser?.uid) {
//            0
//            Log.d(TAG, "getItemViewType: 내 메세지")
//        //다른 사람 보낸 메세지
//        } else {
//            1
//            Log.d(TAG, "getItemViewType: 다른 사람 메세지")
//        }
//
//    }

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