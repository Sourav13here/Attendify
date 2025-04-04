package com.example.attendify.data.repository

import android.util.Log
import com.example.attendify.data.model.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

}











/*
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun storeUserData(user: User): Result<Unit> {
        return try {
            db.collection("users").document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun storeSubjectData(subject: Subject): Result<Unit> {
        return try {
            db.collection("subjects").document(subject.subjectCode).set(subject).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSubjectForTeacher(uid: String, subjectCode: String): Result<Unit> {
        return try {
            val userRef = db.collection("users").document(uid)
            userRef.update("subjectForTeacher", FieldValue.arrayUnion(subjectCode)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserBasicDetails(uid: String): Result<User> {
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            if (snapshot.exists()) {
                val student = snapshot.toObject(User::class.java)
                    ?: throw Exception("Invalid data format")
                Result.success(student)
            } else {
                Result.failure(Exception("Data not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserType(uid: String): Result<String> {
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            val userType = snapshot.getString("userType") ?: "unknown"
            Result.success(userType)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTeacherData(uid: String): Result<Map<String, Any>> {
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            if (snapshot.exists()) {
                Result.success(snapshot.data ?: emptyMap())
            } else {
                Result.failure(Exception("Teacher data not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTeacherSubjects(teacherId: String): Result<List<String>> {
        return try {
            val snapshot = db.collection("users").document(teacherId).get().await()
            if (snapshot.exists()) {
                val subjects = snapshot.get("subjectForTeacher") as? List<*> ?: emptyList<String>()
                val subjectList = subjects.filterIsInstance<String>()
                Result.success(subjectList)
            } else {
                Result.failure(Exception("No subjects found for teacher"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSubjectById(subjectId: String): Result<Subject> {
        return try {
            Log.e("firest", "")
            val snapshot = db.collection("subjects").document(subjectId).get().await()
            Log.e("firest", "$snapshot")
            snapshot.toObject(Subject::class.java)?.let { subject ->
                Log.e("firest", "$subject")
                Result.success(subject)
            } ?: Result.failure(Exception("Subject not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStudentsBySemesterBranch(semester: String, branch: String): Result<List<User>> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("semester", semester)
                .whereEqualTo("branch", branch)
                .get()
                .await()

            val students = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


 */