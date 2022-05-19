package com.example.firebasetutorial.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetutorial.MyApplication
import com.example.firebasetutorial.MyApplication.Companion.auth
import com.example.firebasetutorial.MyApplication.Companion.email
import com.example.firebasetutorial.R
import com.example.firebasetutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.authMainTextView.text = "${MyApplication.email} 로그인"
        initLogOutButton()
        initChangePasswordButton()
        initDeleteUserButton()
        initFirestoreTestButton()
    }

    private fun initLogOutButton() {
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            email = null
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
        val mDialogView: View = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val closeButton: Button =  mDialogView.findViewById<Button>(R.id.closeButton)
        val deleteButton: Button = mDialogView.findViewById<Button>(R.id.deleteButton)

        binding.deleteUserButton.setOnClickListener {
            val mDialogBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mDialogBuilder.show()

            closeButton.setOnClickListener {
                mAlertDialog.dismiss()
            }

            deleteButton.setOnClickListener {
                val user = auth.currentUser!!
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "계정 삭제", Toast.LENGTH_SHORT).show()
                            email = null
                            finish()
                        }
                        else {
                            Toast.makeText(this, "계정 삭제 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    private fun initFirestoreTestButton() {
        binding.firestoreTestButton.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }
    }
}