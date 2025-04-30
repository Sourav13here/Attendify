package com.example.attendify.ui.teacher

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.ui.teacher.components.AttendanceTable
import com.example.attendify.ui.teacher.components.DateSelectionRow
import com.example.attendify.ui.teacher.components.getCurrentDate
import com.example.attendify.ui.teacher.components.getCurrentMonthYear
import com.example.attendify.ui.teacher.components.getNextMonth
import com.example.attendify.ui.teacher.components.getPreviousMonth
import com.example.attendify.ui.theme.AttendifyTheme

@Composable
fun AttendanceSheet(navController: NavController, subjectCode: String ) {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(getCurrentMonthYear()) }

    AppScaffold(
        title = "Computer Networks",
        navController = navController,
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
                    .padding(4.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "CS1809213", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Computer Networks", fontSize = 14.sp)
                }
            }

            // Month Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentMonth = getPreviousMonth(currentMonth)
                    selectedDate = "01" // Reset to 1st of new month
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Month")
                }
                Text(text = currentMonth, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = {
                    currentMonth = getNextMonth(currentMonth)
                    selectedDate = "01" // Reset to 1st of new month
                }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Month")
                }
            }

            // Scrollable Date Selection
            DateSelectionRow(selectedDate, currentMonth) { newDate ->
                selectedDate = newDate
            }

            // Attendance Table
            AttendanceTable()
        }
    }
}





@Preview(showSystemUi = true)
@Composable
fun AttendanceSheetScreenPreview() {
    AttendifyTheme {
        AttendanceSheet(
            navController = rememberNavController(),
            subjectCode = "CD18638162"
        )
    }
}


