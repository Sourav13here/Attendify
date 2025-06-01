package com.example.attendify.ui.teacher

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Attendance
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import generateExcelReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {
    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects

    private val _teacher = MutableStateFlow<Teacher?>(null)
    val teacher: StateFlow<Teacher?> = _teacher

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _isLoadingStudentsList = MutableStateFlow(false)
    val isLoadingStudentsList: StateFlow<Boolean> = _isLoadingStudentsList

    private val _attendanceStatusByEmail = MutableStateFlow<Map<String, Int>>(emptyMap())
    val attendanceStatusByEmail: StateFlow<Map<String, Int>> = _attendanceStatusByEmail

    private val _studentAttendanceInfo = MutableStateFlow<Map<String, Int>>(emptyMap())
    val studentAttendanceInfo: StateFlow<Map<String, Int>> = _studentAttendanceInfo

    init {
        Log.e("LogTeacher", "init")
        fetchTeacherData()
    }

    fun deleteSubject(subject: Subject, context: Context) {
        viewModelScope.launch {
            try {
//                firestoreRepo.deleteSubject(subject.subjectCode
//                    onSuccess = {
//                        Toast.makeText(context, "Subject deleted", Toast.LENGTH_SHORT).show()
//                        loadSubjects(authRepo.getCurrentUser()?.email ?: "")
//                    },
//                    onFailure = {
//                        Toast.makeText(context, "Failed to delete subject", Toast.LENGTH_SHORT).show()
//                    }
//                )
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to delete subject", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun downloadAttendanceReport(
        subject: Subject,
        students: List<Student>,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val attendanceList = firestoreRepo.getAttendanceWithStudentInfo(subject, students)
                Log.e("excel", "$attendanceList")
                val file = generateExcelReport(
                    context,
                    attendanceList,
                    subject
                )
                if (file != null) {
                    Toast.makeText(context, "Report saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to generate report", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error downloading report", Toast.LENGTH_SHORT).show()
            }
        }
    }




    fun loadAttendancePercentages(subjectName: String, branch: String, semester: String) {
        viewModelScope.launch {
            try {
                val result = firestoreRepo.getAttendancePercentages(subjectName, branch, semester)
                _studentAttendanceInfo.value = result
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
                    date = mapOf(date to status),
                    studentEmail = studentEmail,
                    subjectName = subjectName,
                    markedBy = markedBy
                )
                _attendanceStatusByEmail.value = _attendanceStatusByEmail.value.toMutableMap().apply {
                    put(studentEmail, status)
                }
                firestoreRepo.storeAttendance(attendance, branch, semester)
                loadAttendancePercentages(subjectName, branch, semester)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeAttendance(
        studentEmail: String,
        branch: String,
        semester: String,
        subjectName: String,
        date: String
    ) {
        viewModelScope.launch {
            try {
                _attendanceStatusByEmail.value = _attendanceStatusByEmail.value.toMutableMap().apply {
                    remove(studentEmail)
                }
                firestoreRepo.removeAttendance(studentEmail, branch, semester, subjectName, date)
                loadAttendancePercentages(subjectName, branch, semester)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchTeacherData() {
        val userId = authRepo.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            try {
                val teacherData = firestoreRepo.getTeacherDetails(userId)
                _teacher.value = teacherData
                loadSubjects(teacherData.email)
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