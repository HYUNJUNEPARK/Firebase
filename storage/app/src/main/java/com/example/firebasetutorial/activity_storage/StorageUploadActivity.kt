package com.example.firebasetutorial.activity_storage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.R
import com.example.firebasetutorial.baseForPermission.BaseActivity
import com.example.firebasetutorial.databinding.ActivityFirebaseStorageBinding
import com.example.firebasetutorial.key.FirebaseKey.Companion.IMG_DESCRIPTION
import com.google.firebase.storage.StorageReference
import java.io.File

class StorageUploadActivity : BaseActivity() {
    private val binding by lazy { ActivityFirebaseStorageBinding.inflate(layoutInflater) }
    private lateinit var resultListener: ActivityResultLauncher<Intent>
    private lateinit var filePath: String
    private val permissionList: Array<String> = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.emailTextView.text = "${MyApplication.email} 로그인"

        initGalleryOpenButton()
        initResultListener()
        initUploadButton()
        initGoToStorageRecyclerViewActivityButton()
    }

    override fun permissionGranted(requestCode: Int) {
        getImgFromGallery()
    }

    override fun permissionDenied(requestCode: Int) {
        requirePermissions(permissionList, 10)
    }

    private fun initGalleryOpenButton() {
        binding.galleryButton.setOnClickListener {
            requirePermissions(permissionList, 10)
        }
    }

    private fun getImgFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        resultListener.launch(intent)
    }

    private fun initResultListener() {
        resultListener =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { takenImg ->
                if (takenImg.resultCode == Activity.RESULT_OK) {
                    val imgUri: Uri? = takenImg.data?.data

                    Glide.with(this)
                        .load(imgUri)
                        .error(R.drawable.ic_baseline_cancel_24)
                        .override(100, 100)
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_refresh_24)
                        .into(binding.addImageView)
                    val cursor = contentResolver.query(
                        imgUri as Uri,
                        arrayOf<String>(MediaStore.Images.Media.DATA),
                        null, null, null
                    )
                    cursor?.moveToFirst().let {
                        filePath = cursor?.getString(0) as String
                    }
                }
            }
    }

    private fun initUploadButton() {
        binding.uploadButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (binding.addImageView.drawable == null || binding.editTextView.text.isEmpty()) {
                Toast.makeText(this, "이미지 또는 텍스트 없음", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }
            uploadContentsToStore()
        }
    }

    private fun uploadContentsToStore() {
        val data = mapOf(
            "email" to MyApplication.email,
            "description" to binding.editTextView.text.toString()
        )
        Thread {
            MyApplication.db.collection(IMG_DESCRIPTION)
                .add(data)
                .addOnSuccessListener { document ->
                    //Store 에 저장되는 document 의 id 를 이미지의이름으로 하며, 이미지를 Storage 에 저장
                    uploadImgToStorage(document.id)
                }
                .addOnFailureListener { e ->
                    runOnUiThread {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
                    }
                }
        }.start()
    }

    private fun uploadImgToStorage(id: String) {
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imgRef: StorageReference = storageRef.child("images/$id.jpg")
        var file_uri = Uri.fromFile(File(filePath))
        Thread {
            imgRef.putFile(file_uri)
                .addOnSuccessListener {
                    runOnUiThread {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.editTextView.text = null
                        Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    runOnUiThread {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
                    }
                }
        }.start()
    }

    private fun initGoToStorageRecyclerViewActivityButton() {
        binding.loadImageListButton.setOnClickListener {
            val intent = Intent(this, StorageRecyclerViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}