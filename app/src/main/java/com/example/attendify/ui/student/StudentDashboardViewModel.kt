package com.example.attendify.ui.student

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject

import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {
    private val _student = mutableStateOf<Student?>(null)
    val student = _student

    private val _subjects = mutableStateOf<List<Subject>>(emptyList())
    val subjects = _subjects

    private val _subjectAttendancePairs = mutableStateOf<List<Pair<Subject, Int>>>(emptyList())
    val subjectAttendancePairs = _subjectAttendancePairs

    private val _presentDates = MutableStateFlow<List<String>>(emptyList())
    val presentDates: StateFlow<List<String>> = _presentDates

    private val _absentDates = MutableStateFlow<List<String>>(emptyList())
    val absentDates: StateFlow<List<String>> = _absentDates

    init {
        fetchStudentData()
    }

//    fun loadStudentAttendance(branch: String, semester: String) {
//        val email = authRepo.getCurrentUser()?.email ?: return
//        viewModelScope.launch {
//            try {
//                val allAttendance = firestoreRepo.getAllAttendanceForStudent1(email, branch, semester)
//                val present = mutableListOf<String>()
//                val absent = mutableListOf<String>()
//
//                allAttendance.forEach { attendance ->
//                    when (attendance.status) {
//                        1 -> present.add(attendance.date)
//                        0 -> absent.add(attendance.date)
//                    }
//                }
//
//                _presentDates.value = present
//                _absentDates.value = absent
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    private fun fetchStudentData() {
        val userId = authRepo.getCurrentUser()?.uid
        Log.d("StudentDashboardVM", "UserID: $userId")
        if (userId == null) return
        viewModelScope.launch {
            try {
                val studentData = firestoreRepo.getStudentDetails(userId)
                _student.value = studentData
                fetchSubjectsForStudent(studentData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchSubjectsForStudent(student: Student) {
        viewModelScope.launch {
            try {
                val subjectsData = firestoreRepo.getSubjectsByBranchAndSemester(student.branch, student.semester)
                _subjects.value = subjectsData

                val attendanceList = subjectsData.map { subject ->
                    val percentage = firestoreRepo.getAttendanceForStudent(
                        studentId = student.rollNumber,
                        subjectCode = subject.subjectCode
                    )
                    subject to percentage // Pair<Subject, Int>
                }
                _subjectAttendancePairs.value = attendanceList

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _attendanceMap = mutableStateOf<Map<String, Int>>(emptyMap())
    val attendanceMap = _attendanceMap

    private val _totalClasses = mutableStateOf(0)
    val totalClasses = _totalClasses

    private val _attendedClasses = mutableStateOf(0)
    val attendedClasses = _attendedClasses


    fun fetchAttendanceForSubject(
        subjectName: String,
        branch: String,
        semester: String
    ) {
        viewModelScope.launch {
            try {
                val studentEmail = _student.value?.email ?: return@launch
                Log.d("StudentVM", "Fetching attendance for subjectName=$subjectName, branch=$branch, semester=$semester, studentEmail=$studentEmail")

                val attendanceRecords = firestoreRepo.getAllAttendanceForStudent1(
                    subjectName = subjectName,
                    branch = branch,
                    semester = semester,
                    studentEmail = studentEmail
                )

                Log.d("StudentVM", "Fetched attendance records: ${attendanceRecords.size}")
                _attendanceMap.value = attendanceRecords

                _totalClasses.value = attendanceRecords.size
                _attendedClasses.value = attendanceRecords.values.count { it == 1 }

                // ⬇️ Separate present and absent dates
                val present = mutableListOf<String>()
                val absent = mutableListOf<String>()
                attendanceRecords.forEach { (date, status) ->
                    if (status == 1) present.add(date)
                    else absent.add(date)
                }

                _presentDates.value = present
                _absentDates.value = absent

            } catch (e: Exception) {
                Log.e("StudentVM", "Failed to fetch attendance", e)
            }
        }
    }







    fun getAttendancePercentage(): Int {
        return if (_totalClasses.value > 0) {
            (_attendedClasses.value * 100) / _totalClasses.value
        } else {
            0
        }
    }




    fun signOut() {
        viewModelScope.launch {
            try {
                authRepo.signOut()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}