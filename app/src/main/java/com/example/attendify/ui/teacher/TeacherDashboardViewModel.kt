package com.example.attendify.ui.teacher

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Attendance
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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

    private val _attendanceStatusByEmail = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val attendanceStatusByEmail: StateFlow<Map<String, Boolean>> = _attendanceStatusByEmail


    init {
        loadSubjects()
        fetchTeacherData()
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

    fun markAttendance(
        studentEmail: String,
        branch: String,
        semester: String,
        status: Int,
        subjectName: String,
        markedBy: String
    ) {
        viewModelScope.launch {
            try {
                val attendance = Attendance(
                    date = LocalDate.now().toString(),
                    studentEmail = studentEmail,
                    status = status == 1,
                    subjectName = subjectName,
                    markedBy = markedBy
                )
                firestoreRepo.storeAttendance(attendance, branch, semester)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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



    fun loadSubjects() {
        firestoreRepo.getSubjects(
            onSuccess = { _subjects.value = it },
            onFailure = { it.printStackTrace() }
        )
    }

    fun addSubject(subject: Subject) {
        firestoreRepo.addSubject(
            subject,
            onSuccess = { loadSubjects() },
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