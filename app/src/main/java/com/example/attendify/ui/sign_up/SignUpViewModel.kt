package com.example.attendify.ui.sign_up

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _navigateToVerification = MutableStateFlow(false)
    val navigateToVerification: StateFlow<Boolean> = _navigateToVerification.asStateFlow()

    fun createAccount(
        context: Context,
        username: String,
        email: String,
        password: String,
        accountType: String,
        branch: String,
        semester: String,
        rollno: String
    ) {
        if (username.isBlank() || email.isBlank() || password.isBlank() ||
            (accountType == "student" && (branch.isBlank() || semester.isBlank() || rollno.isBlank())) ||
            (accountType == "teacher" && branch.isBlank())
        ) {
            Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            val result = authRepo.signUp(email, password)

            if (result.isSuccess) {
                val uid = result.getOrNull().orEmpty()

                val isHod = Regex("^hod[a-z]+@bvec\\.ac\\.in$").matches(email.trim().lowercase())

                firestoreRepo.storeUserData(
                    uid = uid,
                    username = username,
                    email = email,
                    accountType = accountType,
                    branch = branch,
                    semester = semester,
                    rollno = rollno,
                    isHod = isHod,
                    isVerified = false,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Account created successfully. Please verify your email.",
                            Toast.LENGTH_LONG
                        ).show()
                        _navigateToVerification.value = true
                    },
                    onFailure = { e ->
                        Toast.makeText(context, "Failed to store user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(context, result.exceptionOrNull()?.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun resetNavigationState() {
        _navigateToVerification.value = false
    }
}