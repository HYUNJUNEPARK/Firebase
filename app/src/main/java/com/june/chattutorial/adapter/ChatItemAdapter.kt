package com.june.chattutorial.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.june.chattutorial.R
import com.june.chattutorial.databinding.ItemMyChatBinding
import com.june.chattutorial.databinding.ItemPartnerUserChatBinding
import com.june.chattutorial.key.FBVal.Companion.currentUser
import com.june.chattutorial.key.UserIDPW.Companion.userA_UID
import com.june.chattutorial.key.ViewType.Companion.CURRENT_USER_MESSAGE
import com.june.chattutorial.key.ViewType.Companion.PARTNER_USER_MESSAGE
import com.june.chattutorial.model.ChatItemModel
import java.text.SimpleDateFormat
import java.util.*

class ChatItemAdapter(private val activity: AppCompatActivity) : ListAdapter<ChatItemModel, RecyclerView.ViewHolder>(diffUtil) {
    inner class PartnerUserViewHolder(private val binding: ItemPartnerUserChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItemModel) {
            binding.messageTextView.text = chatItem.message
            binding.dateTextView.text = timeFormat(chatItem)
            binding.profileImage.background = partnerUserProfileDrawable()
        }
    }

    inner class MyChatViewHolder(private val binding: ItemMyChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItemModel) {
            binding.messageTextView.text = chatItem.message
            binding.dateTextView.text = timeFormat(chatItem)
        }
    }

    private fun timeFormat(chatItem: ChatItemModel): String {
        val format = SimpleDateFormat("hh:mm")
        val date = Date(chatItem.sendTime!!)

        return format.format(date).toString()
    }

    private fun partnerUserProfileDrawable(): Drawable {
        val userBResId: Int = R.drawable.user_b
        val userAResId: Int = R.drawable.user_a
        val drawableResId = if (currentUser!!.uid != userA_UID) userAResId else userBResId

        return ResourcesCompat.getDrawable(activity.resources, drawableResId, null)!!
    }

    override fun getItemViewType(position: Int): Int {
        val message = currentList[position]
        return if (message.senderId == currentUser?.uid) {
            CURRENT_USER_MESSAGE
        } else {
            PARTNER_USER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val partnerUserBinding = ItemPartnerUserChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val currentUserBinding = ItemMyChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return when (viewType) {
            CURRENT_USER_MESSAGE -> MyChatViewHolder(currentUserBinding)
            PARTNER_USER_MESSAGE -> PartnerUserViewHolder(partnerUserBinding)
            else -> PartnerUserViewHolder(partnerUserBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentList[position].senderId == currentUser?.uid) {
            val holder = holder as MyChatViewHolder
            holder.bind(currentList[position])
        }
        else {
            val holder = holder as PartnerUserViewHolder
            holder.bind(currentList[position])
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