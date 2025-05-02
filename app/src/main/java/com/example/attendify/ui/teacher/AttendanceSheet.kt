package com.example.attendify.ui.teacher

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.ui.teacher.components.ScrollableDateSelectionRow
import com.example.attendify.ui.teacher.components.StudentList
import com.example.attendify.ui.teacher.components.getCurrentDate
import com.example.attendify.ui.teacher.components.getCurrentMonthYear
import com.example.attendify.ui.teacher.components.getFormattedFullDate
import com.example.attendify.ui.teacher.components.getNextMonth
import com.example.attendify.ui.teacher.components.getPreviousMonth

@Composable
fun AttendanceSheet(
    navController: NavController,
    subjectCode: String,
    subjectName: String,
    branch: String,
    semester: String,
    teacherEmail: String,
    viewModel: TeacherDashboardViewModel
) {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(getCurrentMonthYear()) }
    val students by viewModel.students.collectAsState()
    val isLoadingStudentsList by viewModel.isLoadingStudentsList.collectAsState()
    val fullDate = getFormattedFullDate(selectedDate, currentMonth)

    LaunchedEffect(Unit) {
        viewModel.loadStudents(branch, semester)
    }
    LaunchedEffect(selectedDate, currentMonth) {
        viewModel.loadAttendanceStatusForDate(fullDate, subjectName, branch, semester)
    }
    AppScaffold(
        title = subjectName,
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
        showLogo = false,
        showBackButton = true
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Subject Box
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$subjectCode ($branch - $semester sem)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Month Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentMonth = getPreviousMonth(currentMonth)
                    selectedDate = "01" // Reset to 1st of new month
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Month"
                    )
                }
                Text(text = currentMonth, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = {
                    currentMonth = getNextMonth(currentMonth)
                    selectedDate = "01" // Reset to 1st of new month
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Month"
                    )
                }
            }

            ScrollableDateSelectionRow(selectedDate, currentMonth) { newDate ->
                selectedDate = newDate
            }

            // Student List
            StudentList(
                students = students,
                subjectName = subjectName,
                markedBy = teacherEmail,
                loading = isLoadingStudentsList,
                viewModel = viewModel,
                date = fullDate
            )
        }
    }
}
