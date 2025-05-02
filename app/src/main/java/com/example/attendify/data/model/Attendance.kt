package com.example.attendify.data.model

data class Attendance(
    val date: Map<String, Int> = emptyMap(),
    val studentEmail: String = "",
    val subjectName: String = "",
    val markedBy: String = ""
)
