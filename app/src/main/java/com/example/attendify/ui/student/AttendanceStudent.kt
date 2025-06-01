package com.example.attendify.ui.student

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.ui.student.components.AttendanceDetailsDialog
import com.example.attendify.ui.student.components.AttendanceReportCard
import com.example.attendify.ui.student.components.CalendarView
import com.example.attendify.ui.student.components.InstructionsDialog
import com.example.attendify.ui.student.components.SubjectInfoCard
import com.example.attendify.ui.theme.AttendifyTheme
import java.time.LocalDate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


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
    val attendanceMap by remember { viewModel.attendanceMap }
    val localAttendanceMap = remember { mutableStateMapOf<String, Int>() }
    val combinedMap = attendanceMap.toMutableMap().apply { putAll(localAttendanceMap) }

    val totalClasses = combinedMap.size
    val attendedClasses = combinedMap.values.count { it == 1 }
    val percentage = if (totalClasses > 0) (attendedClasses * 100) / totalClasses else 0

    var showDetailsDialog by remember { mutableStateOf(false) }
    var showInstructionsDialog by remember { mutableStateOf(false) }

    val instructionsKey = "instructions_shown_${subjectCode}_${studentEmail}"
    val context = LocalContext.current

    LaunchedEffect(subjectName, student) {
        if (student != null) {
            viewModel.fetchAttendanceForSubject(subjectName, branch, semester)
        }

        val prefs = context.getSharedPreferences("attendify_prefs", Context.MODE_PRIVATE)
        val hasShown = prefs.getBoolean(instructionsKey, false)
        if (!hasShown) {
            showInstructionsDialog = true
            // Set flag to true so it won't show next time
            prefs.edit().putBoolean(instructionsKey, true).apply()
        }
    }

    AppScaffold(
        title = "",
        navController = navController,
        showBackButton = true,
        contentDescriptionBackButton = "Back",
        titleTextStyle = MaterialTheme.typography.titleMedium,
        actions = {
            IconButton(onClick = { showInstructionsDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Instructions",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
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
                attendanceMap = combinedMap,
                initialDate = LocalDate.now(),
                localAttendanceMap = localAttendanceMap,
                viewmodel = viewModel
            )
            Spacer(modifier = Modifier.height(20.dp))

            AttendanceReportCard(
                totalClasses = totalClasses,
                attendedClasses = attendedClasses,
                percentage = percentage,
                onClick = { showDetailsDialog = true }
            )
        }
    }

    if (showDetailsDialog) {
        AttendanceDetailsDialog(
            combinedMap = combinedMap,
            onDismiss = { showDetailsDialog = false }
        )
    }

    if (showInstructionsDialog) {
        InstructionsDialog(onDismiss = { showInstructionsDialog = false })
    }
}







