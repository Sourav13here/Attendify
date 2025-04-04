package com.example.attendify.ui.sign_up

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository
): ViewModel() {
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
            (accountType == "Student" && (branch.isBlank() || semester.isBlank() || rollno.isBlank()))
        ) {
            Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            val result = authRepo.signUp(email, password)
            if (result.isSuccess) {
                val uid = result.getOrNull()
                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                // You can now save additional user info to Firestore using `uid`, if needed
            } else {
                Toast.makeText(context, result.exceptionOrNull()?.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


}