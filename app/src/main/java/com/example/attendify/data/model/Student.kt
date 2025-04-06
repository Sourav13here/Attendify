package com.example.attendify.data.model

import com.google.firebase.firestore.PropertyName

data class Student(
    val email: String = "",
    val name: String = "",
    @get:PropertyName("isVerified") @set:PropertyName("isVerified")
    var isVerified: Boolean = false,
    val branch: String = "",
    val semester: String = "",
    val rollNumber: String = ""
)
