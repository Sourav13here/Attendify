package com.example.attendify.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendify.data.model.Subject
import com.example.attendify.data.repository.AuthRepository
import com.example.attendify.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init{
        loadSubjects()
    }

    fun loadSubjects() {
        firestoreRepo.getSubjects(
            onSuccess = { _subjects.value = it },
            onFailure = {it.printStackTrace()}
        )
    }

    fun addSubject(subject: Subject) {
        firestoreRepo.addSubject(
            subject,
            onSuccess = { loadSubjects() },
            onFailure = {it.printStackTrace() }
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