package com.example.attendify.data.repository

import android.util.Log
import com.example.attendify.data.model.Attendance
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject
import com.example.attendify.data.model.Teacher
import com.google.firebase.firestore.FieldValue
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
            val studentSnapshot = db.collection("Student").document(userId).get().await()
            val student = studentSnapshot.toObject(Student::class.java)
            if (studentSnapshot.exists() && student != null) return student to "Student"

            val teacherSnapshot = db.collection("Teacher").document(userId).get().await()
            val teacher = teacherSnapshot.toObject(Teacher::class.java)
            if (teacherSnapshot.exists() && teacher != null) return teacher to "Teacher"

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

    fun addSubject(subject: Subject, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("Subjects")
            .add(subject)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getSubjects(
        teacherEmail: String,
        onSuccess: (List<Subject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("Subjects")
            .whereEqualTo("createdBy", teacherEmail)
            .get()
            .addOnSuccessListener { snapshot ->
                val subjects = snapshot.toObjects(Subject::class.java)
                onSuccess(subjects)
            }
            .addOnFailureListener { onFailure(it) }
    }

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

    fun getVerifiedStudents(branch: String, semester: String, onResult: (List<Student>) -> Unit) {
        if (branch.isBlank() || semester.isBlank()) {
            onResult(emptyList())
            return
        }

        db.collection("Student")
            .whereEqualTo("isVerified", true)
            .whereEqualTo("branch", branch)
            .whereEqualTo("semester", semester)
            .get()
            .addOnSuccessListener { snapshot ->
                val students = snapshot.toObjects(Student::class.java)
                onResult(students)
            }
    }

    fun storeAttendance(attendance: Attendance, branch: String, semester: String) {
        val attendanceRef = db.collection("Attendance")
            .document(branch)
            .collection(semester)
            .document(attendance.subjectName)
            .collection("students")
            .document(attendance.studentEmail)

        attendanceRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val currentDateMap = mutableMapOf<String, Int>()

                if (documentSnapshot.exists()) {
                    // Document exists, update the date map
                    val existingAttendance = documentSnapshot.toObject(Attendance::class.java)
                    currentDateMap.putAll(existingAttendance?.date ?: emptyMap())
                }

                // Overwrite or add new date entry
                val (newDate, newStatus) = attendance.date.entries.first()
                currentDateMap[newDate] = newStatus

                // Build updated attendance object
                val updatedAttendance = Attendance(
                    date = currentDateMap,
                    studentEmail = attendance.studentEmail,
                    subjectName = attendance.subjectName,
                    markedBy = attendance.markedBy
                )

                // Save updated attendance object
                attendanceRef.set(updatedAttendance)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Attendance marked or updated successfully.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Failed to mark/update attendance: ", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error reading document: ", e)
            }
    }


    suspend fun getAttendanceForDate(
        date: String,
        subjectName: String,
        branch: String,
        semester: String
    ): Map<String, Int> {
        val attendanceMap = mutableMapOf<String, Int>()
        val snapshot = db.collection("Attendance")
            .document(branch)
            .collection(semester)
            .document(subjectName)
            .collection(date)
            .get()
            .await()

        for (doc in snapshot.documents) {
            val studentEmail = doc.getString("studentEmail")
            val status = doc.getLong("status")?.toInt() // Safe conversion
            if (studentEmail != null && status != null) {
                attendanceMap[studentEmail] = status
            }
        }
        return attendanceMap
    }

    suspend fun deleteUser(uid: String, collection: String) {
        db.collection(collection).document(uid).delete().await()
    }

    suspend fun getStudentDetails(userId: String): Student {
        val document = db.collection("Student").document(userId).get().await()
        return document.toObject(Student::class.java) ?: throw Exception("Student not found")
    }

    suspend fun getTeacherDetails(userId: String): Teacher {
        val document = db.collection("Teacher").document(userId).get().await()
        return document.toObject(Teacher::class.java) ?: throw Exception("Teacher not found")
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
            val present = documents.count { it.getBoolean("status") == true }

            if (total == 0) 0 else (present * 100) / total
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getTotalClasses(subjectName: String, branch: String, semester: String): Int {
        return try {
            val snapshot = db.collection("Attendance")
                .document(branch)
                .collection(semester)
                .document(subjectName)
                .get()
                .await()

            // Return total number of classes
            snapshot.getLong("totalClasses")?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }


    suspend fun getAllAttendanceForStudent1(
        subjectCode: String,
        branch: String,
        semester: String,
        studentId: String
    ): Map<String, Boolean> {
        val attendanceMap = mutableMapOf<String, Boolean>()
        val snapshots = db.collection("attendance")
            .document(subjectCode)
            .collection("dates")
            .get()
            .await()

        for (doc in snapshots.documents) {
            val date = doc.id
            val studentSnapshot = doc.reference
                .collection("students")
                .document(studentId)
                .get()
                .await()

            if (studentSnapshot.exists()) {
                val isPresent = studentSnapshot.getBoolean("isPresent") ?: false
                attendanceMap[date] = isPresent
            }
        }
        return attendanceMap
    }
}
