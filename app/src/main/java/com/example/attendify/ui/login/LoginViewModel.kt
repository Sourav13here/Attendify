package com.example.attendify.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _navigateToStudentDashboard = MutableStateFlow(false)
    val navigateToStudentDashboard: StateFlow<Boolean> = _navigateToStudentDashboard.asStateFlow()

    private val _navigateToTeacherDashboard = MutableStateFlow(false)
    val navigateToTeacherDashboard: StateFlow<Boolean> = _navigateToTeacherDashboard.asStateFlow()

    private val _navigateToStatus = MutableStateFlow(false)
    val navigateToStatus: StateFlow<Boolean> = _navigateToStatus.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _passwordResetMessage = MutableStateFlow<String?>(null)
    val passwordResetMessage: StateFlow<String?> = _passwordResetMessage

    private val _passwordResetMessage2 = MutableStateFlow<String?>(null)
    val passwordResetMessage2: StateFlow<String?> = _passwordResetMessage2

    fun login(email: String, password: String) {
        when {
            email.isBlank() -> {
                _loginError.value = "Email cannot be empty"
                return
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _loginError.value = "Invalid email format"
                return
            }

            password.isBlank() -> {
                _loginError.value = "Password cannot be empty"
                return
            }

            password.length < 6 -> {
                _loginError.value = "Password must be at least 6 characters"
                return
            }
        }

        viewModelScope.launch {
            val result = authRepo.logIn(email, password)
            result.fold(
                onSuccess = {
                    checkUserAndNavigate()
                },
                onFailure = { e ->
                    val message = when ((e as? FirebaseAuthException)?.errorCode) {
                        "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                        "ERROR_WRONG_PASSWORD" -> "Incorrect password"
                        "ERROR_INVALID_EMAIL" -> "The email address is badly formatted"
                        "ERROR_USER_DISABLED" -> "This account has been disabled"
                        "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later"
                        "ERROR_CREDENTIAL_ALREADY_IN_USE" -> "This credential is already linked to another account"
                        "ERROR_INVALID_CREDENTIAL" -> "Invalid credentials. Please try again"
                        "ERROR_OPERATION_NOT_ALLOWED" -> "This operation is not allowed. Please contact support"
                        else -> e.message ?: "Login failed. Please try again"
                    }
                    _loginError.value = message
                }
            )
        }
    }

    private suspend fun checkUserAndNavigate() {
        val user = authRepo.getCurrentUser() ?: run {
            _loginError.value = "User not found."
            return
        }

        user.reload()
        if (!user.isEmailVerified) {
            _navigateToStatus.value = true
            return
        }

        val userData = firestoreRepo.getUser(user.uid)
        if (userData == null) {
            _loginError.value = "No user data found in Firestore."
            return
        }

        val (userObj, type) = userData

        when (type) {
            "Teacher" -> {
                val teacher = userObj as? Teacher
                when {
                    teacher?.isHod == true -> {
                        firestoreRepo.updateIsVerified(user.uid, "Teacher", true)
                        _navigateToTeacherDashboard.value = true
                    }

                    teacher?.isVerified == true -> {
                        _navigateToTeacherDashboard.value = true
                    }

                    else -> {
                        _navigateToStatus.value = true
                    }
                }
            }

            "Student" -> {
                val student = userObj as? Student
                if (student?.isVerified == true) {
                    _navigateToStudentDashboard.value = true
                } else {
                    _navigateToStatus.value = true
                }
            }

            else -> {
                _loginError.value = "Unrecognized user type."
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        when {
            email.isBlank() -> {
                _passwordResetMessage2.value = "Email cannot be empty"
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _passwordResetMessage2.value = "Invalid email format"
                return
            }
        }

        viewModelScope.launch {
            val result = authRepo.sendPasswordResetEmail(email)
            result.fold(
                onSuccess = {
                    _passwordResetMessage.value = "Reset link sent to $email"
                },
                onFailure = {
                    _passwordResetMessage.value = it.message ?: "Failed to send reset link"
                }
            )
        }
    }

    fun clearPasswordResetMessage() {
        _passwordResetMessage.value = null
        _passwordResetMessage2.value = null
    }


    fun resetNavigationState() {
        _navigateToTeacherDashboard.value = false
        _navigateToStudentDashboard.value = false
        _navigateToStatus.value = false
        _loginError.value = null
    }

    fun clearLoginError() {
        _loginError.value = null
    }

}
