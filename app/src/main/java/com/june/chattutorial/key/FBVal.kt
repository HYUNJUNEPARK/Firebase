package com.june.chattutorial.key

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBVal: MultiDexApplication() {
    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var firebaseDBReference: DatabaseReference
        var currentUser: FirebaseUser? = null

        fun initCurrentUser() {
            currentUser = auth?.currentUser
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        firebaseDBReference = Firebase.database.reference
    }
}