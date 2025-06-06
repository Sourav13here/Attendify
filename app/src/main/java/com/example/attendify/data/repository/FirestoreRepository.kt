package com.example.attendify.data.repository

import android.util.Log
import com.example.attendify.data.model.Attendance
import com.example.attendify.data.model.AttendanceWithStudentInfo
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Subject
import com.example.attendify.data.model.Teacher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    val db: FirebaseFirestore
) {

    suspend fun deleteSubject(subjectId: String) {
        db.collection("Subjects").document(subjectId).delete().await()
    }

    suspend fun getAttendanceWithStudentInfo(
        subject: Subject,
        students: List<Student>
    ): List<AttendanceWithStudentInfo> {
        val resultList = mutableListOf<AttendanceWithStudentInfo>()
        try {
            // Map passed students by email for quick lookup
            val emailToStudent = students.associateBy({ it.email }, { Pair(it.rollNumber, it.name) })

            // Fetch attendance records for the subject
            val attendanceSnapshot = db.collection("Attendance")
                .document(subject.branch)
                .collection(subject.semester)
                .document(subject.subjectName)
                .collection("students")
                .get()
                .await()

            for (doc in attendanceSnapshot.documents) {
                val attendance = doc.toObject(Attendance::class.java) ?: continue
                val studentInfo = emailToStudent[attendance.studentEmail] ?: Pair("N/A", "N/A")
                resultList.add(
                    AttendanceWithStudentInfo(
                        rollNumber = studentInfo.first,
                        name = studentInfo.second,
                        attendanceMap = attendance.date
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultList
    }


    suspend fun generateAttendanceReport(
        branch: String,
        semester: String,
        subjectName: String
    ): Map<String, Int> {
        val report = mutableMapOf<String, Int>()

        try {
            val studentsSnapshot = db.collection("Attendance")
                .document(branch)
                .collection(semester)
                .document(subjectName)
                .collection("students")
                .get()
                .await()

            for (doc in studentsSnapshot.documents) {
                val attendance = doc.toObject(Attendance::class.java) ?: continue
                val dateRecords = attendance.date
                val totalClasses = dateRecords.size
                val presentCount = dateRecords.values.count { it == 1 }  // 1 means present

                val percentage = if (totalClasses > 0) (presentCount * 100) / totalClasses else 0
                report[attendance.studentEmail] = percentage
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return report
    }

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

    fun removeAttendance(
        studentEmail: String,
        branch: String,
        semester: String,
        subjectName: String,
        date: String
    ) {
        val attendanceRef = db.collection("Attendance")
            .document(branch)
            .collection(semester)
            .document(subjectName)
            .collection("students")
            .document(studentEmail)

        attendanceRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val attendance = documentSnapshot.toObject(Attendance::class.java)
                    val dateMap = attendance?.date?.toMutableMap() ?: mutableMapOf()
                    dateMap.remove(date)

                    if (dateMap.isEmpty()) {
                        // Delete entire document if no dates remain
                        attendanceRef.delete()
                            .addOnSuccessListener {
                                Log.d("Firestore", "Attendance document deleted as no dates left.")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error deleting attendance document: ", e)
                            }
                    } else {
                        // Update the document with the remaining dates
                        attendance?.let {
                            val updatedAttendance = it.copy(date = dateMap)
                            attendanceRef.set(updatedAttendance)
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Attendance date removed successfully.")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "Error updating attendance: ", e)
                                }
                        }

                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error reading attendance document: ", e)
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
            .collection("students")
            .get()
            .await()

        for (doc in snapshot.documents) {
            val attendance = doc.toObject(Attendance::class.java)
            val status = attendance?.date?.get(date)
            val studentEmail = attendance?.studentEmail

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
                .collection("students")
                .get()
                .await()

            if (snapshot.documents.isNotEmpty()) {
                val attendance = snapshot.documents[0].toObject(Attendance::class.java)
                attendance?.date?.size ?: 0
            } else {
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


    suspend fun getAllAttendanceForStudent1(
        subjectName: String,
        branch: String,
        semester: String,
        studentEmail: String
    ): Map<String, Int> {
        return try {
            val db = FirebaseFirestore.getInstance()
            val documentSnapshot = db.collection("Attendance")
                .document(branch)
                .collection(semester)
                .document(subjectName)
                .collection("students")
                .document(studentEmail)
                .get()
                .await()

            val dateMap = documentSnapshot.get("date") as? Map<*, *>
            dateMap?.mapNotNull { (key, value) ->
                val date = key as? String
                val status = (value as? Long)?.toInt()
                if (date != null && status != null) date to status else null
            }?.toMap() ?: emptyMap()

        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    suspend fun getAttendancePercentages(
        subjectName: String,
        branch: String,
        semester: String
    ): Map<String, Int> {
        val map = mutableMapOf<String, Int>()

        val snapshot = db.collection("Attendance")
            .document(branch)
            .collection(semester)
            .document(subjectName)
            .collection("students")
            .get()
            .await()

        for (doc in snapshot.documents) {
            val attendance = doc.toObject(Attendance::class.java)
            val records = attendance?.date ?: continue

            val total = records.size
            val present = records.values.count { it == 1 }

            val percentage = if (total > 0) (present * 100) / total else 0
            map[attendance.studentEmail] = percentage
        }

        return map
    }


}