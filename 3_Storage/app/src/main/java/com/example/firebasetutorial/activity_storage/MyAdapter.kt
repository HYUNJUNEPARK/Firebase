package com.example.firebasetutorial.activity_storage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.R
import com.example.firebasetutorial.databinding.ItemStorageBinding
import com.google.firebase.storage.StorageReference

class MyAdapter(val context: Context, private val itemIdList: MutableList<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemStorageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemStorageBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemIdList.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        val id = itemIdList[position]
        updateImgByGlide(id, myViewHolder)
    }

    private fun updateImgByGlide(id: String, holder: MyViewHolder) {
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imgRef: StorageReference = storageRef.child("images/${id}.jpg")
        imgRef.downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.ic_baseline_refresh_24)
                    .error(R.drawable.ic_baseline_cancel_24)
                    .override(120, 120)
                    .centerCrop()
                    .into(holder.binding.storageImageView)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }
}