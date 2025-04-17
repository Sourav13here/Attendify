package com.example.attendify.ui.verification

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    fun verifyAndSaveUser(
        userType: String,
        username: String,
        branch: String,
        semester: String?,
        roll: String?
    ) {
        val userMap = hashMapOf(
            "username" to username,
            "branch" to branch
        ).apply {
            if (userType == "student") {
                semester?.let { put("semester", it) }
                roll?.let { put("roll", it) }
            }
        }

        val collection = if (userType == "student") "Student" else "Teacher"

        viewModelScope.launch {
            try {
               //Query Firestore to check if the user already exists
                val querySnapshot = firestore.collection(collection)
                    .whereEqualTo("username",username)
                    .get()
                    .await()

                //If user doesn't exist, save the user
                if(querySnapshot.isEmpty){
                    firestore.collection(collection)
                        .add(userMap)
                        .addOnSuccessListener { Log.d("Firestore", "User saved") }
                        .addOnFailureListener { e -> Log.e("Firestore", "Error saving user", e) }
                }else run {
                    Log.d("Firestore", "User already exists")
                }

            } catch (e: Exception) {
                Log.e("Firestore", "Exception saving user", e)
            }
        }
    }

    private val _navigateToStudentDashboard = MutableStateFlow(false)
    val navigateToStudentDashboard: StateFlow<Boolean> = _navigateToStudentDashboard.asStateFlow()

    private val _navigateToTeacherDashboard = MutableStateFlow(false)
    val navigateToTeacherDashboard: StateFlow<Boolean> = _navigateToTeacherDashboard.asStateFlow()

    fun refreshData() {
        viewModelScope.launch {
            val user = authRepo.getCurrentUser()
            val userId = user?.uid ?: return@launch

            user.reload()
            if (user.isEmailVerified) {
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
