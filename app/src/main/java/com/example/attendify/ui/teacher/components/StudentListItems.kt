package com.example.attendify.ui.teacher.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.data.model.Student
import com.example.attendify.ui.teacher.TeacherDashboardViewModel

@Composable
fun StudentListItems(
    index: Int,
    student: Student,
    date: String,
    subjectName: String,
    onAttendanceMarked: (status: Int) -> Unit,
    viewModel: TeacherDashboardViewModel
) {
    var showFullNameDialog by remember { mutableStateOf(false) }

    var isPresent by remember { mutableIntStateOf(-1) }
    val attendanceStatus by viewModel.attendanceStatusByEmail.collectAsState()
    val status = attendanceStatus[student.email]

    LaunchedEffect(status) {
        if (status != null) {
            isPresent = status
        } else {
            isPresent = -1
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = student.rollNumber,
            fontSize = 14.sp,
            modifier = Modifier.width(120.dp)
        )

        Divider(color = Color.Gray, modifier = Modifier
            .width(1.dp)
            .height(24.dp))

        Text(
            text = student.name,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(160.dp)
                .clickable { showFullNameDialog = true }
        )

        Divider(modifier = Modifier.width(1.dp).height(24.dp), color = Color.Black)

        // Present Button
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(if (isPresent == 1 || status == 1) Color.Green else Color(0xFFC8E6C9))
                .clickable() {
                    isPresent = 1
                    onAttendanceMarked(1)
                }
        )

        // Absent Button
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(if (isPresent == 0 || status == 0) Color.Red else Color(0xFFFFCDD2))
                .clickable() {
                    isPresent = 0
                    onAttendanceMarked(0)
                }
        )

        Divider(modifier = Modifier.width(1.dp).height(24.dp), color = Color.Black)

        Text(
            text = "90", // Placeholder
            fontSize = 14.sp,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
    }

    if (showFullNameDialog) {
        AlertDialog(
            onDismissRequest = { showFullNameDialog = false },
            title = { Text("Full Name:") },
            text = { Text(student.name) },
            confirmButton = {
                Button(onClick = { showFullNameDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
