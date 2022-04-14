package com.example.firebasetutorial.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.MyApplication.Companion.TAG
import com.example.firebasetutorial.MyApplication.Companion.auth
import com.example.firebasetutorial.R
import com.example.firebasetutorial.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var mDialogView: View


    lateinit var closeButton: Button
    lateinit var deleteButton: Button

    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.authMainTextView.text = "${MyApplication.email} 로그인"
        initLogOutButton()
        initChangePasswordButton()
        initDeleteUserButton()
    }

    private fun initLogOutButton() {
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email = null
            finish()
        }
    }

    private fun initChangePasswordButton() {
        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initDeleteUserButton() {
        mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        binding.deleteUserButton.setOnClickListener {
            val mDialogBuilder = AlertDialog.Builder(this).setView(mDialogView)
            closeButton = mDialogView.findViewById<Button>(R.id.closeButton)
            deleteButton = mDialogView.findViewById<Button>(R.id.deleteButton)
            val mAlertDialog = mDialogBuilder.show()

            closeButton.setOnClickListener {
                mAlertDialog.dismiss()
            }

            deleteButton.setOnClickListener {
                //val user = auth.currentUser!!
                user = auth.currentUser!!
                Log.d(TAG, "currentUser: $user ")
                Log.d(TAG, "before Delete : ${auth.currentUser!!}")

                auth.currentUser!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "계정 삭제", Toast.LENGTH_SHORT).show()
                            MyApplication.email = null
                            Log.d(TAG, "initDeleteUserButton: ${MyApplication.email}")
                            Log.d(TAG, "currentUser: $user ")
                            Log.d(TAG, "after Delete : ${auth.currentUser!!}")
                            finish()

                        }
                        else {
                            Toast.makeText(this, "계정 삭제 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}