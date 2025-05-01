package com.example.attendify.ui.student

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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.ui.student.components.AttendanceReportCard
import com.example.attendify.ui.student.components.CalendarView
import com.example.attendify.ui.student.components.SubjectInfoCard
import com.example.attendify.ui.theme.AttendifyTheme

@Composable
fun AttendanceStudent(navController: NavController, subjectInfo: String) {
    val (subjectName, subjectCode) = subjectInfo.split("|")
    val totalClasses = 15
    val attendedClasses = 10
    val percentage = (attendedClasses * 100) / totalClasses

    AppScaffold(
        title = subjectName,
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

            CalendarView()
            Spacer(modifier = Modifier.height(20.dp))

            AttendanceReportCard(
                totalClasses = totalClasses,
                attendedClasses = attendedClasses,
                percentage = percentage
            )
        }
    }
}





//@Preview(showSystemUi = true)
//@Composable
//fun DisplayAttendanceStudent() {
//    AttendifyTheme {
//        AttendanceStudent(rememberNavController())
//    }
//}
