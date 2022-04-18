package com.example.firebasetutorial.activity_storage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.MyApplication.Companion.TAG
import com.example.firebasetutorial.R
import com.example.firebasetutorial.databinding.ItemStorageBinding

class MyAdapter(val context: Context, val itemIdList: MutableList<String>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemStorageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemStorageBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemIdList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemIdList[position]

        updateImgByGlide(data, holder)
    }

    private fun updateImgByGlide(data: String, holder: MyViewHolder) {
        MyApplication.storage
            .reference
            .child("images/${data}.jpg")
            .downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.ic_baseline_cancel_24)
                    .into(holder.binding.storageImageView)
                Log.d(TAG, "onBindViewHolder: $uri")
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }
}