package com.example.attendify.ui.teacher

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Attendance
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {
    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects

    private val _teacher = mutableStateOf<Teacher?>(null)
    val teacher = _teacher

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _isLoadingStudentsList = MutableStateFlow(false)
    val isLoadingStudentsList: StateFlow<Boolean> = _isLoadingStudentsList

    private val _attendanceStatusByEmail = MutableStateFlow<Map<String, Int>>(emptyMap())
    val attendanceStatusByEmail: StateFlow<Map<String, Int>> = _attendanceStatusByEmail

    init {
//        loadSubjects()
        fetchTeacherData()
    }

    fun loadAttendanceStatusForDate(
        date: String,
        subjectName: String,
        branch: String,
        semester: String
    ) {
        viewModelScope.launch {
            try {
                val result = firestoreRepo.getAttendanceForDate(
                    date, subjectName, branch, semester
                )
                _attendanceStatusByEmail.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markAttendance(
        studentEmail: String,
        branch: String,
        semester: String,
        status: Int,
        subjectName: String,
        markedBy: String,
        date: String
    ) {
        viewModelScope.launch {
            try {
                val attendance = Attendance(
                    date = date,
                    studentEmail = studentEmail,
                    status = status,
                    subjectName = subjectName,
                    markedBy = markedBy
                )
                _attendanceStatusByEmail.value = _attendanceStatusByEmail.value.toMutableMap().apply {
                    put(studentEmail, status)
                }
                firestoreRepo.storeAttendance(attendance, branch, semester)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchTeacherData() {
        val userId = authRepo.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            try {
                val teacherData = firestoreRepo.getTeacherDetails(userId)
                _teacher.value = teacherData
//                fetchSubjectsForStudent(studentData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadStudents(branch: String, semester: String) {
        viewModelScope.launch {
            _isLoadingStudentsList.value = true
            try {
                firestoreRepo.getVerifiedStudents(branch, semester) { studentList ->
                    _students.value = studentList
                    _isLoadingStudentsList.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _isLoadingStudentsList.value = false
            }
        }
    }

    fun loadSubjects(teacherEmail: String) {
        firestoreRepo.getSubjects(
            onSuccess = { _subjects.value = it },
            onFailure = { it.printStackTrace() },
            teacherEmail = teacherEmail,
        )
    }

    fun addSubject(teacherEmail: String,subject: Subject, context: Context) {
        if (subject.subjectName.isBlank() || subject.subjectCode.isBlank()) {
            Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            return
        }
        firestoreRepo.addSubject(
            subject = subject,
            onSuccess = { loadSubjects(teacherEmail) },
            onFailure = { it.printStackTrace() }
        )
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