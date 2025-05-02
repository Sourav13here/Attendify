package com.example.attendify.data.model

data class Attendance(
    val date: String = "",
    val studentEmail: String = "",
    val status: Int = -1,
    val subjectName: String = "",
    val markedBy: String = ""
)



