package com.example.attendify.ui.student

import SubjectWithAttendance
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
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {
    private val _student = mutableStateOf<Student?>(null)
    val student = _student

    private val _subjects = mutableStateOf<List<Subject>>(emptyList())
    val subjects = _subjects

    private val _subjectwithattendance = mutableStateOf<List<SubjectWithAttendance>>(emptyList())
    val subjectwithattendance = _subjectwithattendance

    init {
        fetchStudentData()
    }

    private fun fetchStudentData() {
        val userId = authRepo.getCurrentUser()?.uid ?: return
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
                        studentId = student.rollNumber, // or UID based on how you store attendance
                        subjectCode = subject.subjectCode
                    )
                    SubjectWithAttendance(
                        subject = subject,
                        subjectCode = subject.subjectCode,
                        attendancePercentage = percentage
                    )
                }
                _subjectwithattendance.value = attendanceList
            } catch (e: Exception) {
                e.printStackTrace()
            }
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