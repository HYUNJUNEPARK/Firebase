package com.example.firebasetutorial.activity_storage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.databinding.ActivityStorageListBinding
import com.example.firebasetutorial.key.FirebaseKey.Companion.IMG_DESCRIPTION

class StorageRecyclerViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStorageListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        initRecyclerView()
    }

    private fun initRecyclerView() {
        //Store 에 저장된 document 의 id 를 Adapter 에 전달
        MyApplication.db.collection(IMG_DESCRIPTION)
            .get()
            .addOnSuccessListener { snapshot ->
                val itemIdList = mutableListOf<String>()
                for(document in snapshot) {
                    binding.progressBar.visibility = View.INVISIBLE
                    val itemId = document.id
                    itemIdList.add(itemId)
                }
                binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
                binding.recyclerView.adapter = MyAdapter(this, itemIdList)
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
            }
    }
}