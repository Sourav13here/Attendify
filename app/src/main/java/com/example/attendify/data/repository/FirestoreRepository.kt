package com.example.attendify.data.repository

import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    fun storeUserData(
        uid: String,
        username: String,
        email: String,
        accountType: String,
        branch: String = "",
        semester: String = "",
        rollno: String = "",
        isHod: Boolean = false,
        isVerified: Boolean = false,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val collectionPath = if (accountType == "Student") "Student" else "Teacher"

        val userData = if (accountType == "Student") {
            Student(
                name = username,
                email = email,
                isVerified = isVerified,
                branch = branch,
                semester = semester,
                rollNumber = rollno
            )
        } else {
            Teacher(
                name = username,
                email = email,
                isVerified = isVerified,
                isHod = isHod,
                branch = branch
            )
        }

        db.collection(collectionPath).document(uid)
            .set(userData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
