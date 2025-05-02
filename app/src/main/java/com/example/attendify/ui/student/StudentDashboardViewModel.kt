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

    init {
        fetchStudentData()
    }

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

    private val _attendanceMap = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val attendanceMap = _attendanceMap

    private val _totalClasses = mutableStateOf(0)
    val totalClasses = _totalClasses

    private val _attendedClasses = mutableStateOf(0)
    val attendedClasses = _attendedClasses


    fun fetchAttendanceForSubject(
        subjectCode: String,
        branch: String,
        semester: String
    ) {
        viewModelScope.launch {
            try {
                val studentId = _student.value?.rollNumber ?: return@launch

                // Fetch all attendance records for the subject
                val attendanceRecords = firestoreRepo.getAllAttendanceForStudent(
                    subjectCode = subjectCode,
                    branch = branch,
                    semester = semester,
                    studentId = studentId
                )

                _attendanceMap.value = attendanceRecords
                _totalClasses.value = attendanceRecords.size
                _attendedClasses.value = attendanceRecords.values.count { it }

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