package com.example.attendify.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import com.example.attendify.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    fun checkLoggedInUser() {
        val userId = authRepo.getCurrentUserId()
        if (userId != null) {
            viewModelScope.launch {
                val result = firestoreRepo.getUser(userId)
                result?.let { (user, accountType) ->
                    when (accountType) {
                        "Student" -> {
                            val student = user as Student
                            _startDestination.value = if (student.isVerified) {
                                NavRoutes.StudentDashboard.route
                            } else {
                                NavRoutes.VerificationStatus.route
                            }
                        }

                        "Teacher" -> {
                            val teacher = user as Teacher
                            _startDestination.value = if (teacher.isVerified) {
                                NavRoutes.TeacherDashboard.route
                            } else {
                                NavRoutes.VerificationStatus.route
                            }
                        }
                    }
                } ?: run {
                    _startDestination.value = NavRoutes.LoginPage.route
                }
            }
        } else {
            _startDestination.value = NavRoutes.LoginPage.route
        }
    }

}
