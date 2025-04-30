package com.example.attendify.data.model

import com.google.firebase.firestore.PropertyName

data class Teacher(
    val uid : String = "",
    val email: String = "",
    val name: String = "",
    @get:PropertyName("isVerified") @set:PropertyName("isVerified")
    var isVerified: Boolean = false,
    @get:PropertyName("isHod") @set:PropertyName("isHod")
    var isHod: Boolean = false,
    val branch: String = ""
)
