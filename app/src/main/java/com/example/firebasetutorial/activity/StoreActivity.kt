package com.example.firebasetutorial.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.MyApplication.Companion.TAG
import com.example.firebasetutorial.databinding.ActivityStoreBinding
import com.example.firebasetutorial.key.FirebaseKey.Companion.USERS
import com.example.firebasetutorial.model.User
import com.example.firebasetutorial.model.UserForUpdateDelete
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StoreActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStoreBinding.inflate(layoutInflater) }
    private lateinit var db: FirebaseFirestore
    private val sb = StringBuilder()
    var userObj: UserForUpdateDelete? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Firebase.firestore
        binding.emailTextView.text = MyApplication.email.toString()

        initWriteButton()
        initReadButton()
        initSearchButton()
    }

    private fun initWriteButton() {
        binding.firestoreWriteButton.setOnClickListener {
            val email = MyApplication.email.toString()
            val name = binding.nameTextView.text.toString()
            val admin = binding.adminCheckbox.isChecked

            if (name == "") {
                Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = User(email, name, admin)

            Thread {
                db.collection(USERS)
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        runOnUiThread {
                            Toast.makeText(this, "DB 저장", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        runOnUiThread {
                            Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
                        }
                        Log.w(TAG, "Error adding document", e)
                    }
            }.start()
        }
    }

    private fun initReadButton() {
        binding.firestoreREADButton.setOnClickListener {
            binding.updateButton.visibility = View.INVISIBLE
            binding.deleteButton.visibility = View.INVISIBLE
            binding.updateNameTextView.visibility = View.INVISIBLE

            binding.progressBar.visibility = View.VISIBLE

            Thread {
                db.collection(USERS)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        for (document in snapshot) {
                            val id: String = document.id + "\n"
                            sb.append(id)
                        }
                        runOnUiThread {
                            binding.idTextView.text = sb
                            binding.progressBar.visibility = View.GONE
                            sb.setLength(0)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.d(TAG, "Error: $e")
                        runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
            }.start()
        }
    }

    private fun initSearchButton() {
        binding.searchButton.setOnClickListener {
            binding.idTextView.text = ""
            val searchId = binding.searchIdTextView.text.toString()
            if (searchId == "") {
                Toast.makeText(this, "ID를 확인해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            Thread {
                val docRef = db.collection(USERS).document(searchId)
                docRef.get()
                    .addOnSuccessListener { document ->
                        initDeleteButton(document.id)
                        initUpdateButton(document.id)

                        if (document != null) {
                            userObj = document.toObject(UserForUpdateDelete::class.java)
                            sb.append(
                                "name : ${document.data?.get("name")} \n" +
                                        "email : ${document.data?.get("email")} \n" +
                                        "isAdmin : ${document.data?.get("isAdmin")}"
                            )
                            runOnUiThread {
                                binding.idTextView.text = sb
                                binding.searchIdTextView.text = null
                                binding.updateButton.visibility = View.VISIBLE
                                binding.deleteButton.visibility = View.VISIBLE
                                binding.updateNameTextView.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                            }
                            sb.setLength(0)
                        } else {
                            runOnUiThread {
                                Toast.makeText(this, "Empty document", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        runOnUiThread {
                            Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
                        }
                    }
            }.start()
        }
    }

    private fun initDeleteButton(id: String) {
        binding.deleteButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            Thread {
                db.collection(USERS)
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        runOnUiThread {
                            Toast.makeText(this, "데이터 삭제", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                    .addOnFailureListener {
                        runOnUiThread {
                            Toast.makeText(this, "데이터 삭제 실패", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
            }.start()
        }
    }

    private fun initUpdateButton(id: String) {
        binding.updateButton.setOnClickListener {
            val userInput = binding.updateNameTextView.text.toString()
            if (userInput == "") {
                Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            Thread {
                db.collection(USERS)
                    .document(id)
                    .update("name", userInput)
                    .addOnSuccessListener {
                        runOnUiThread {
                            Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }

                    }
                    .addOnFailureListener {
                        runOnUiThread {
                            Toast.makeText(this, "수정 실패", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
            }.start()
        }
    }
}