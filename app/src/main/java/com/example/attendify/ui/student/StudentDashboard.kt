package com.example.attendify.ui.student


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomIconButton
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.student.components.AttendanceReportCard
import com.example.attendify.ui.verification.components.LogoutConfirmationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun StudentDashboard(navController: NavController, viewModel: StudentDashboardViewModel) {
    var showLogOutDialog by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        val userId = viewModel.authRepo.getCurrentUser()?.uid
        if (userId == null) {
            navController.navigate(NavRoutes.LoginPage.route) {
                popUpTo(NavRoutes.StudentDashboard.route) { inclusive = true }
            }
        }
    }

    if (showLogOutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.signOut()
                    navController.navigate(NavRoutes.LoginPage.route) {
                        popUpTo(NavRoutes.StudentDashboard.route) { inclusive = true }
                    }
                }
                showLogOutDialog = false
            },
            onDismiss = {
                showLogOutDialog = false
            }
        )
    }

    AppScaffold(
        title = "STUDENT DASHBOARD",
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        actions = {
            CustomIconButton(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE57373), shape = CircleShape),
                imageVector = Icons.AutoMirrored.Filled.Logout,
                onClick = {
                    showLogOutDialog = true
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val student = viewModel.student.value
            val subjectsWithAttendance = viewModel.subjectAttendancePairs.value

            if (student != null) {
                Text(student.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${student.branch} - ${student.semester} sem", fontSize = 16.sp)
            } else {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(16.dp))



            LazyColumn(
                modifier = Modifier
                    .background(Color(0xFFD1C4E9), RoundedCornerShape(16.dp))
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (subjectsWithAttendance.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                } else {
                    items(subjectsWithAttendance) { (subject, percentage) ->
                        AttendanceCard(
                            subject = subject.subjectName,
                            title = subject.subjectCode,
                            percentage = percentage,
                            onClick = {
                                student?.let {
                                    navController.navigate(
                                        "attendance_student/${subject.subjectName}/${subject.subjectCode}/${it.branch}/${it.semester}/${it.email}"
                                    )
                                }
                            }
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun AttendanceCard(subject: String, title: String, percentage: Int, onClick: () -> Unit) {
    val color = when {
        percentage >= 75 -> Color.Green
        percentage in 40..74 -> Color.Yellow
        percentage in 1..39 -> Color.Red
        else -> Color.Black
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .background(color = Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(subject, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 14.sp)
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(color, shape = CircleShape),

            contentAlignment = Alignment.Center
        ) {
            Text(text = "$percentage", color = Color.White, fontSize = 12.sp)
        }
    }
}


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewStudentDashboard() {
//    val mockViewModel = object : StudentDashboardViewModel() {
//        // You can override signOut or other functions here if needed
//    }
//
//    StudentDashboard(
//        navController = rememberNavController(),
//        viewmodel = mockViewModel
//    )
//}


