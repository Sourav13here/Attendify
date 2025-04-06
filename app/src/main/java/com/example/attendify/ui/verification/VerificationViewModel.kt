package com.example.attendify.ui.verification

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _navigateToStudentDashboard = MutableStateFlow(false)
    val navigateToStudentDashboard: StateFlow<Boolean> = _navigateToStudentDashboard.asStateFlow()

    private val _navigateToTeacherDashboard = MutableStateFlow(false)
    val navigateToTeacherDashboard: StateFlow<Boolean> = _navigateToTeacherDashboard.asStateFlow()

    fun refreshData() {
        Log.e("verification", "refreshdata")
        viewModelScope.launch {
            Log.e("verification", "viewmodelscope")
            val user = authRepo.getCurrentUser()
            val userId = user?.uid ?: return@launch

            user.reload()
            Log.e("verification", "email verified: ${user.isEmailVerified}")

            if (user.isEmailVerified) {
                Log.e("verification", "if email verified")
                val userData = firestoreRepo.getUser(userId)

                userData?.let { (userObj, type) ->
                    if (type == "Teacher") {
                        val teacher = userObj as? Teacher
                        if (teacher?.isHod == true) {
                            // Update isVerified to true in Firestore
                            firestoreRepo.updateIsVerified(userId, "Teacher", true)
                            _navigateToTeacherDashboard.value = true
                        } else if (teacher?.isVerified == true) {
                            _navigateToTeacherDashboard.value = true
                        }
                    } else if (type == "Student") {
                        val student = userObj as? Student
                        if (student?.isVerified == true) {
                            _navigateToStudentDashboard.value = true
                        }
                    }
                }

            }
            Log.e("verification", "not verified")
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
