package com.example.attendify.data.model

data class Student(
    val email: String = "",
    val name: String = "",
    val isVerified: Boolean = false,
    val branch: String = "",
    val semester: String = "",
    val rollNumber: String = ""
)
