package com.example.attendify.ui.student

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.ui.student.components.AttendanceReportCard
import com.example.attendify.ui.student.components.CalendarView
import com.example.attendify.ui.student.components.SubjectInfoCard
import com.example.attendify.ui.theme.AttendifyTheme
import java.time.LocalDate

@Composable
fun AttendanceStudent(
    navController: NavController,
    subjectName: String,
    subjectCode: String,
    branch: String,
    semester: String,
    studentEmail: String,
    viewModel: StudentDashboardViewModel = hiltViewModel()
) {
    // Observe the student data and attendance map
    val student by remember { viewModel.student }
    val attendanceMap by remember { viewModel.attendanceMap } // Firestore data
    val localAttendanceMap = remember { mutableStateMapOf<String, Int>() } // Temporary changes

    // Combine Firestore data (attendanceMap) and local data (localAttendanceMap)
    val combinedMap = attendanceMap.toMutableMap().apply {
        putAll(localAttendanceMap) // Merge local changes with Firestore data
    }

    val totalClasses = combinedMap.size
    val attendedClasses = combinedMap.values.count { it == 1 }
    val percentage = if (totalClasses > 0) (attendedClasses * 100) / totalClasses else 0

    Log.d("AttendanceStudent", "Combined Map: $combinedMap")
    Log.d("AttendanceStudent", "Total Classes: $totalClasses, Attended Classes: $attendedClasses, Percentage: $percentage")

    // Trigger fetch only after student is loaded or if subject changes
    LaunchedEffect(subjectName, student) {
        if (student != null) {
            Log.d("AttendanceStudent", "Student is available, fetching attendance")
            viewModel.fetchAttendanceForSubject(
                subjectName = subjectName,
                branch = branch,
                semester = semester
            )
        } else {
            Log.d("AttendanceStudent", "Student is null, skipping attendance fetch")
        }
    }

    AppScaffold(
        title = "",
        navController = navController,
        showBackButton = true,
        contentDescriptionBackButton = "Back",
        titleTextStyle = MaterialTheme.typography.titleMedium
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Display the subject info
            SubjectInfoCard(subjectName = subjectName, subjectCode = subjectCode)
            Spacer(modifier = Modifier.height(20.dp))

            // Display the calendar for the student
            CalendarView(
                attendanceMap = combinedMap, // Pass combined map (Firestore + local)
                initialDate = LocalDate.now(),
                localAttendanceMap = localAttendanceMap,
                viewmodel = viewModel
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Display the attendance report card
            AttendanceReportCard(
                totalClasses = totalClasses,
                attendedClasses = attendedClasses,
                percentage = percentage
            )
        }
    }
}



