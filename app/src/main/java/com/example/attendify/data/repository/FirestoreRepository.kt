package com.example.attendify.data.repository

import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject
import com.example.attendify.data.model.Teacher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    val db: FirebaseFirestore
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
        val collectionPath = if (accountType == "student") "Student" else "Teacher"

        val userData = if (accountType == "student") {
            Student(
                uid = uid,
                name = username,
                email = email,
                isVerified = isVerified,
                branch = branch,
                semester = semester,
                rollNumber = rollno
            )
        } else {
            Teacher(
                uid = uid,
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
//    suspend fun getUserDetails(uid: String, accountType: String): Map<String, Any>? {
//        return try {
//            val snapshot = db.collection(accountType).document(uid).get().await()
//            if (snapshot.exists()) snapshot.data else null
//        } catch (e: Exception) {
//            null
//        }
//    }

    fun getUnverifiedStudents(branch: String, semester: String, onResult: (List<Student>) -> Unit) {
        if (branch.isBlank() || semester.isBlank()) {
            onResult(emptyList())
            return
        }

        db.collection("Student")
            .whereEqualTo("isVerified", false)
            .whereEqualTo("branch", branch)
            .whereEqualTo("semester", semester)
            .get()
            .addOnSuccessListener { snapshot ->
                val students = snapshot.toObjects(Student::class.java)
                onResult(students)
            }
    }


    fun getUnverifiedTeachers(branch: String, onResult: (List<Teacher>) -> Unit) {
        if (branch.isBlank()) {
            onResult(emptyList())
            return
        }

        db.collection("Teacher")
            .whereEqualTo("isVerified", false)
            .whereEqualTo("branch", branch)
            .get()
            .addOnSuccessListener { snapshot ->
                val teachers = snapshot.toObjects(Teacher::class.java)
                onResult(teachers)
            }
    }


    suspend fun deleteUser(uid: String, collection: String) {
        db.collection(collection).document(uid).delete().await()
    }

    suspend fun getStudentDetails(userId: String): Student {
        val document = db.collection("Student").document(userId).get().await()
        return document.toObject(Student::class.java) ?: throw Exception("Student not found")
    }

    suspend fun getSubjectsByBranchAndSemester(branch: String, semester: String): List<Subject> {
        return try {
            val snapshot = db.collection("Subjects")
                .whereEqualTo("branch", branch)
                .whereEqualTo("semester", semester)
                .get()
                .await()

            snapshot.toObjects(Subject::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    suspend fun getAttendanceForStudent(studentId: String, subjectCode: String): Int {
        return try {
            val snapshot = db.collection("Attendance")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("subjectCode", subjectCode)
                .get()
                .await()

            val documents = snapshot.documents
            val total = documents.size
            val present = documents.count { it.getBoolean("present") == true }

            if (total == 0) 0 else (present * 100) / total
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }



}
