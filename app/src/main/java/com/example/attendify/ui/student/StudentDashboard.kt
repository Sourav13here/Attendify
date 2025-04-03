package com.example.attendify.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomIconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight

@Composable
fun StudentDashboard(navController: NavController) {
    AppScaffold(
        title = "STUDENT DASHBOARD",
        navController = navController,
        actions = {
            CustomIconButton(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE57373), shape = CircleShape),
                imageVector = Icons.AutoMirrored.Filled.Logout,
                onClick = {}
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
            Spacer(modifier = Modifier.height(16.dp))

            Text("John Smith", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("CSE - 6th sem", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .background(Color(0xFFD1C4E9), RoundedCornerShape(16.dp))
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                item{
                    repeat(7) {
                            index ->
                        AttendanceCard(subject = "CS1809213", title = "Computer Networks", percentage = listOf(100, 75, 40, 20, 0, 100, 100)[index])
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceCard(subject: String, title: String, percentage: Int) {
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

@Preview(showSystemUi = true)
@Composable
fun DisplayStudentDashboard() {
    StudentDashboard(rememberNavController())
}
