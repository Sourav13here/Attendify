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
    val student by remember { viewModel.student }

    // Trigger fetch only after student is loaded
    LaunchedEffect(subjectName, student) {
        if (student != null) {
            Log.d("AttendanceStudent", "Student is available, fetching attendance")
            viewModel.fetchAttendanceForSubject(
                subjectName = subjectName,   // Use 'subjectName' instead of 'subjectCode'
                branch = branch,
                semester = semester
            )

        } else {
            Log.d("AttendanceStudent", "Student is null, skipping attendance fetch")
        }
    }

    val attendanceMap by remember { viewModel.attendanceMap }
    val totalClasses by remember { viewModel.totalClasses }
    val attendedClasses by remember { viewModel.attendedClasses }
    val percentage = viewModel.getAttendancePercentage()

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

            SubjectInfoCard(subjectName = subjectName, subjectCode = subjectCode)
            Spacer(modifier = Modifier.height(20.dp))

            CalendarView(
                attendanceMap = attendanceMap,
                initialDate = LocalDate.now(),
                viewmodel = viewModel
            )
            Spacer(modifier = Modifier.height(20.dp))

            AttendanceReportCard(
                totalClasses = totalClasses,
                attendedClasses = attendedClasses,
                percentage = percentage
            )
        }
    }
}
