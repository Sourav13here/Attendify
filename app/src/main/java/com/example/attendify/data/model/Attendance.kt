package com.example.attendify.data.model

data class Attendance(
    val date: Map<String, Int> = emptyMap(),
    val studentEmail: String = "",
    val subjectName: String = "",
    val markedBy: String = ""
)
data class StudentAttendanceInfo(
    val studentEmail: String,
    val percentage: Int
)


data class AttendanceWithStudentInfo(
    val rollNumber: String,
    val name: String,
    val attendanceMap: Map<String, Int>
)
