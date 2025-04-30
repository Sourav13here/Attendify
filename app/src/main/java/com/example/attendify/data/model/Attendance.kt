package com.example.attendify.data.model

data class Attendance(
    val date: String = "",
    val studentEmail: String = "",
    val status: Boolean? = null,
    val subjectName: String = "",
    val markedBy: String = ""
)



