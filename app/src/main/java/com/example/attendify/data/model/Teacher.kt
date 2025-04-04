package com.example.attendify.data.model

data class Teacher(
    val email: String = "",
    val name: String = "",
    val isVerified: Boolean = false,
    val isHod: Boolean = false,
    val branch: String = ""
)
