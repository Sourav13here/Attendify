package com.example.attendify.data.repository

import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject
import com.example.attendify.data.model.Teacher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
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


    suspend fun getUser(userId: String): Pair<Any, String>? {
        return try {
            // Check Student collection
            val studentSnapshot = db.collection("Student").document(userId).get().await()
            val student = studentSnapshot.toObject(Student::class.java)
            if (studentSnapshot.exists() && student != null) {
                return student to "Student"
            }

            // Check Teacher collection
            val teacherSnapshot = db.collection("Teacher").document(userId).get().await()
            val teacher = teacherSnapshot.toObject(Teacher::class.java)
            if (teacherSnapshot.exists() && teacher != null) {
                return teacher to "Teacher"
            }

            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateIsVerified(userId: String, collection: String, value: Boolean) {
        db.collection(collection).document(userId)
            .update("isVerified", value)
            .await()
    }
    fun addSubject(
        subject: Subject,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("Subjects")
            .add(subject)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getSubjects(onSuccess: (List<Subject>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("Subjects")
            .get()
            .addOnSuccessListener { snapshot ->
                val subjects = snapshot.toObjects(Subject::class.java)
                onSuccess(subjects)
            }
            .addOnFailureListener { onFailure(it) }
    }

}
