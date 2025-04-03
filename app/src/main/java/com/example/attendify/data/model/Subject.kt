package com.example.attendify.data.model

data class Subject(
    val branch: String = "",
    val name: String = "",
    val semester: String = "",
    val subjectCode: String = "",
    val teacherId: List<String> = emptyList()
)
