package com.example.attendify.data.model
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val userType: String = "",
    val branch: String? = null,
    val semester: String? = null,
    val rollNumber: String? = null,
    val subjectForTeacher: List<String> = emptyList()
)