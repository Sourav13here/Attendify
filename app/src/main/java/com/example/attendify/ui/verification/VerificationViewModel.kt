package com.example.attendify.ui.verification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.model.UnverifiedCount
import com.example.attendify.data.repository.FirestoreRepository
import com.example.attendify.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

sealed class UserData {
    data class StudentData(val student: Student) : UserData()
    data class TeacherData(val teacher: Teacher) : UserData()
}

@ExperimentalCoroutinesApi
@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private var currentBranch: String = ""
    private var currentSemester: String = ""


    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _navigateToStudentDashboard = MutableStateFlow(false)
    val navigateToStudentDashboard: StateFlow<Boolean> = _navigateToStudentDashboard.asStateFlow()

    private val _navigateToTeacherDashboard = MutableStateFlow(false)
    val navigateToTeacherDashboard: StateFlow<Boolean> = _navigateToTeacherDashboard.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    private val _showEmailNotVerifiedBox = MutableStateFlow(false)
    val showEmailNotVerifiedBox: StateFlow<Boolean> = _showEmailNotVerifiedBox.asStateFlow()

    private val _unverifiedStudents = MutableStateFlow<List<Student>>(emptyList())
    val unverifiedStudents: StateFlow<List<Student>> = _unverifiedStudents.asStateFlow()

    private val _unverifiedTeachers = MutableStateFlow<List<Teacher>>(emptyList())
    val unverifiedTeachers: StateFlow<List<Teacher>> = _unverifiedTeachers.asStateFlow()

    private val _unverifiedCounts = MutableStateFlow<List<UnverifiedCount>>(emptyList())
    val unverifiedCounts = _unverifiedCounts.asStateFlow()
    private val _unverifiedTeacherCounts = MutableStateFlow<List<UnverifiedCount>>(emptyList())
    val unverifiedTeacherCounts = _unverifiedTeacherCounts.asStateFlow()



    private var emailVerificationJob: Job? = null

    fun startEmailVerificationPolling() {
        if (emailVerificationJob?.isActive == true) return  // Prevent duplicates

        emailVerificationJob = viewModelScope.launch {
            while (true) {
                try {
                    val firebaseUser = auth.currentUser ?: break
                    firebaseUser.reload()  // Refresh user info

                    if (firebaseUser.isEmailVerified) {
                        _showEmailNotVerifiedBox.value = false
                        refreshData()  // Will navigate to dashboard if verified
                        break  // Exit the loop
                    } else {
                        _showEmailNotVerifiedBox.value = true
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "Error checking email: ${e.localizedMessage}"
                    break
                }

                delay(1000L)  // Wait for 1 second
            }
        }
    }

    fun stopEmailVerificationPolling() {
        emailVerificationJob?.cancel()
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }


    // Function to verify and save user data
    fun verifyAndSaveUser(
        userType: String,
        username: String,
        branch: String,
        semester: String?,
        roll: String?
    ) {
        if (username.isBlank() || branch.isBlank()) {
            _errorMessage.value = "Username and branch are required"
            return
        }

        if (userType == "student" && (semester.isNullOrBlank() || roll.isNullOrBlank())) {
            _errorMessage.value = "Semester and roll are required for students"
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: run {
                    _errorMessage.value = "No authenticated user found"
                    return@launch
                }
                val result = firestoreRepository.getUser(userId)

                // Check if the user exists in Firestore
                if (result == null) {
                    _errorMessage.value = "User not found"
                    return@launch
                }

                val (user, type) = result
                _userType.value = type

                // Check if the user is verified
                when (user) {
                    is Student -> {
                        _userData.value = UserData.StudentData(user)
                        if (user.isVerified) {
                            _navigateToStudentDashboard.value = true
                        } else {
                            _errorMessage.value =
                                "Your account is not verified. Please contact admin."
                        }
                    }

                    is Teacher -> {
                        _userData.value = UserData.TeacherData(user)
                        if (user.isVerified) {
                            _navigateToTeacherDashboard.value = true
                        } else {
                            _errorMessage.value =
                                "Your account is not verified. Please contact admin."
                        }
                    }

                    else -> {
                        _errorMessage.value = "Unknown user type"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error verifying user: ${e.localizedMessage}"
                Log.e("VerificationViewModel", "Verification failed", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUnverifiedUsers(accountType: String, branch: String, semester: String = "") {
        currentBranch = branch
        currentSemester = semester

        if (accountType.isBlank()) {
            Log.w("VerificationViewModel", "fetchUnverifiedUsers called with empty accountType")
            return
        }

        viewModelScope.launch {
            Log.d("VerificationViewModel", "Fetching users: accountType=$accountType, branch=$branch, semester=$semester")
            if (accountType == "student") {
                firestoreRepository.getUnverifiedStudents(branch, semester) {
                    _unverifiedStudents.value = it
                    Log.d("VerificationViewModel", "Fetched ${it.size} unverified students")
                }
            } else {
                firestoreRepository.getUnverifiedTeachers(branch) {
                    _unverifiedTeachers.value = it
                    Log.d("VerificationViewModel", "Fetched ${it.size} unverified teachers")
                }
            }
        }
    }


    fun approveUser(uid: String, accountType: String) {
        viewModelScope.launch {
            firestoreRepository.updateIsVerified(
                uid,
                if (accountType == "student") "Student" else "Teacher",
                true
            )
            fetchUnverifiedUsers(accountType, currentBranch, currentSemester) // re-fetch
        }
    }

    fun rejectUser(uid: String, accountType: String) {
        viewModelScope.launch {
            firestoreRepository.deleteUser(
                uid,
                if (accountType == "student") "Student" else "Teacher"
            )
            fetchUnverifiedUsers(accountType, currentBranch, currentSemester) // re-fetch
        }
    }

    // Refresh data if needed
    fun refreshData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val firebaseUser = auth.currentUser
                val id = firebaseUser?.uid ?: return@launch

                firebaseUser.reload()

                if (!firebaseUser.isEmailVerified) {
                    _showEmailNotVerifiedBox.value = true
                    return@launch
                } else {
                    _showEmailNotVerifiedBox.value = false
                }

                val userData = firestoreRepository.getUser(id)
                userData?.let { (userObj, type) ->
                    _userType.value = type
                    when (userObj) {
                        is Teacher -> {
                            _userData.value = UserData.TeacherData(userObj)
                            if (userObj.isHod) {
                                firestoreRepository.updateIsVerified(id, "Teacher", true)
                                _navigateToTeacherDashboard.value = true
                            } else if (userObj.isVerified) {
                                _navigateToTeacherDashboard.value = true
                            }
                        }

                        is Student -> {
                            _userData.value = UserData.StudentData(userObj)
                            if (userObj.isVerified) {
                                _navigateToStudentDashboard.value = true
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to refresh data: ${e.localizedMessage}"
                Log.e("VerificationViewModel", "Error refreshing data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun computeUnverifiedCounts() {
        val branches = Constants.BRANCHES
        val semesters = Constants.SEMESTERS

        viewModelScope.launch {
            // Compute student counts (by branch and semester)
            val studentCounts = coroutineScope {
                branches.flatMap { branch ->
                    semesters.map { semester ->
                        async {
                            val students = suspendCancellableCoroutine<List<Student>> { cont ->
                                firestoreRepository.getUnverifiedStudents(branch, semester) {
                                    cont.resume(it, onCancellation = null)
                                }
                            }

                            UnverifiedCount(
                                branch = branch,
                                semester = semester,
                                count = students.size
                            )
                        }
                    }
                }.awaitAll()
            }

            // Compute teacher counts (by branch only)
            val teacherCounts = coroutineScope {
                branches.map { branch ->
                    async {
                        val teachers = suspendCancellableCoroutine<List<Teacher>> { cont ->
                            firestoreRepository.getUnverifiedTeachers(branch) {
                                cont.resume(it, onCancellation = null)
                            }
                        }

                        UnverifiedCount(
                            branch = branch,
                            semester = "",  // no semester for teachers
                            count = teachers.size
                        )
                    }
                }.awaitAll()
            }
            // Combine both lists
            val combinedCounts = (studentCounts + teacherCounts)
                .groupBy { Pair(it.branch, it.semester) }
                .map { (key, group) ->
                    UnverifiedCount(
                        branch = key.first,
                        semester = key.second,
                        count = group.sumOf { it.count }
                    )
                }

            _unverifiedCounts.value = combinedCount
        }
    }


    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}