package com.example.firebasetutorial.activity_storage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.MyApplication.Companion.TAG
import com.example.firebasetutorial.baseForPermission.BaseActivity
import com.example.firebasetutorial.databinding.ActivityFirebaseStorageBinding
import com.example.firebasetutorial.key.FirebaseKey.Companion.IMG_DESCRIPTION
import com.google.firebase.storage.StorageReference
import java.io.File

class StorageActivity : BaseActivity() {
    private val binding by lazy { ActivityFirebaseStorageBinding.inflate(layoutInflater) }
    private val permissionList: Array<String> = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private lateinit var resultListener: ActivityResultLauncher<Intent>
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.emailTextView.text = "${MyApplication.email} 로그인"

        initUploadButton()
        initOpenGalleryButton()
        goToStorageListActivityButton()
        initResultListener()
    }

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

    private fun initResultListener() {
        resultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Glide.with(this)
                    .load(result.data?.data)
                    .into(binding.addImageView)

                Log.d(TAG, "StorActivity: ${result.data?.data}")

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

        var file = Uri.fromFile(File(filePath))

        Thread {
            imgRef.putFile(file)
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

    private fun goToStorageListActivityButton() {
        binding.loadImageListButton.setOnClickListener {
            val intent = Intent(this, StorageRecyclerViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}