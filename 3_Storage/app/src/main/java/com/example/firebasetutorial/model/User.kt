package com.example.firebasetutorial.model

data class User(
    var email: String ?= null,
    var name: String,
    @JvmField var isAdmin: Boolean
)
