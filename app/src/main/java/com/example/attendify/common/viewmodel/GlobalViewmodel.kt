package com.example.attendify.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewmodel @Inject constructor(
    val authRepo: AuthRepository,
) : ViewModel() {
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
