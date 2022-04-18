package com.example.firebasetutorial.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.firebasetutorial.base.BaseActivity
import com.example.firebasetutorial.databinding.ActivityFirebaseStorageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

class FirebaseStorageActivity : BaseActivity() {
    private val binding by lazy { ActivityFirebaseStorageBinding.inflate(layoutInflater) }
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private val permissionList: Array<String> = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private lateinit var resultListener: ActivityResultLauncher<Intent>
    private lateinit var filePath: String

    override fun permissionGranted(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        resultListener.launch(intent)


    }
    override fun permissionDenied(requestCode: Int) {
        requirePermissions(permissionList, 10)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initUploadButton()
        initOpenGalleryButton()
        initResultListener()


    }

    private fun initResultListener() {
        resultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Glide.with(this)
                    .load(result.data?.data)
                    .into(binding.addImageView)

                val cursor = contentResolver.query(
                    result.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA),
                    null,
                    null,
                    null
                )
                
                cursor?.moveToFirst().let { 
                    filePath = cursor?.getString(0) as String
                }
            }
        }
    }

    private fun initOpenGalleryButton() {
        binding.galleryButton.setOnClickListener {
            requirePermissions(permissionList, 10)
        }
    }

    private fun initUploadButton() {
        binding.uploadButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            
            if (binding.addImageView.drawable == null) {
                Toast.makeText(this, "이미지 없음", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }
            
            storage = Firebase.storage
            storageRef = storage.reference
            val date = System.currentTimeMillis().toString()
            val imgRef: StorageReference = storageRef.child("images/$date.jpg")
            
            var file = Uri.fromFile(File(filePath))
            imgRef.putFile(file)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
                }
        }
    }
}