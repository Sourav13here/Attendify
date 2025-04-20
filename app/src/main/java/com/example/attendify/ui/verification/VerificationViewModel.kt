package com.example.attendify.ui.verification

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    // State management with SavedStateHandle
    val userData = savedStateHandle.getStateFlow("userData", null as Map<String, Any>?)
    val userType = savedStateHandle.getStateFlow("userType", "")

    // Regular state flows
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _navigateToStudentDashboard = MutableStateFlow(false)
    val navigateToStudentDashboard: StateFlow<Boolean> = _navigateToStudentDashboard

    private val _navigateToTeacherDashboard = MutableStateFlow(false)
    val navigateToTeacherDashboard: StateFlow<Boolean> = _navigateToTeacherDashboard

    fun clearError() {
        _errorMessage.value = null
    }

    fun fetchUserData(userType: String) {
        val collection = if (userType == "student") "Student" else "Teacher"
        val userId = authRepo.getCurrentUser()?.uid ?: return

        viewModelScope.launch {
            try {
                val docSnapshot = firestore.collection(collection).document(userId).get().await()
                Log.d("FirestoreDebug", "Fetched data: ${docSnapshot.data}")

                if (docSnapshot.exists()) {
                    val userMap = docSnapshot.data as? Map<String, Any> ?: emptyMap()
                    savedStateHandle["userData"] = docSnapshot.data ?: emptyMap()
                    Log.d("FirestoreDebug", "Saved to state: $userMap")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch data: ${e.message}"
                Log.e("FirestoreDebug", "Fetch error", e)
            }
        }
    }

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

        val userMap = hashMapOf(
            "username" to username,
            "branch" to branch
        ).apply {
            if (userType == "student") {
                put("semester", semester ?: "")
                put("roll", roll ?: "")
            }
        }

        val collection = if (userType == "student") "Student" else "Teacher"
        savedStateHandle["userType"] = userType

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authRepo.getCurrentUser()?.uid ?: run {
                    _errorMessage.value = "No authenticated user found"
                    return@launch
                }

                val docRef = firestore.collection(collection).document(userId)
                val snapshot = docRef.get().await()

                if (!snapshot.exists()) {
                    docRef.set(userMap).await()
                    Log.d("Firestore", "User saved successfully")
                    savedStateHandle["userData"] = userMap
                } else {
                    Log.d("Firestore", "User already exists")
                    docRef.update(userMap as Map<String, Any>).await()
                }

                fetchUserData(userType)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to save user: ${e.localizedMessage}"
                Log.e("Firestore", "Exception saving user", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshData() {
        val currentUserType = userType.value
        if (currentUserType.isBlank()) {
            _errorMessage.value = "User type not set"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = authRepo.getCurrentUser() ?: run {
                    _errorMessage.value = "No authenticated user found"
                    return@launch
                }

                user.reload().await()
                if (!user.isEmailVerified) {
                    _errorMessage.value = "Email not verified yet"
                    return@launch
                }

                val userId = user.uid
                val userDataResult = firestoreRepo.getUser(userId)

                userDataResult?.let { (userObj, type) ->
                    val dataMap = when (userObj) {
                        is Student -> mapOf(
                            "username" to userObj.name,
                            "branch" to userObj.branch,
                            "semester" to userObj.semester,
                            "roll" to userObj.rollNumber
                        )

                        is Teacher -> mapOf(
                            "username" to userObj.name,
                            "branch" to userObj.branch
                        )

                        else -> emptyMap()
                    }

                    savedStateHandle["userData"] = dataMap

                    when (type) {
                        "Teacher" -> _navigateToTeacherDashboard.value = true
                        "Student" -> _navigateToStudentDashboard.value = true
                    }
                } ?: run {
                    _errorMessage.value = "User data not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Refresh failed: ${e.localizedMessage}"
                Log.e("Verification", "Refresh error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                authRepo.signOut()
                savedStateHandle.remove<Map<String, Any>>("userData")
                savedStateHandle.remove<String>("userType")
            } catch (e: Exception) {
                _errorMessage.value = "Sign out failed: ${e.localizedMessage}"
                Log.e("Verification", "Sign out error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}