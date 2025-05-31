package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.attendify.data.model.Student
import com.example.attendify.ui.teacher.TeacherDashboardViewModel
import com.example.attendify.ui.theme.DarkGreen
import com.example.attendify.ui.theme.DarkRed

@Composable
fun StudentListItems(
    index: Int,
    student: Student,
    subjectName: String,
    onAttendanceMarked: (status: Int) -> Unit,
    viewModel: TeacherDashboardViewModel
) {
    var showFullNameDialog by remember { mutableStateOf(false) }
    var showFullRollDialog by remember { mutableStateOf(false) }
    var isPresent by remember { mutableIntStateOf(-1) }

    val attendanceStatus by viewModel.attendanceStatusByEmail.collectAsState()
    val studentPercentages by viewModel.studentAttendanceInfo.collectAsState()

    val status = attendanceStatus[student.email]
    val percent = studentPercentages[student.email] ?: 0

    LaunchedEffect(status) {
        isPresent = status ?: -1
    }

    LaunchedEffect(studentPercentages) {
        viewModel.loadAttendancePercentages(
            subjectName = subjectName,
            branch = student.branch,
            semester = student.semester
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White)
            .padding(8.dp)
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Roll Number
        Text(
            text = student.rollNumber,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(0.35f)
                .clickable { showFullRollDialog = true }
        )

        // Full Roll Dialog
        if (showFullRollDialog) {
            AlertDialog(
                onDismissRequest = { showFullRollDialog = false },
                title = { Text("Full Roll Number:") },
                text = { Text(student.rollNumber) },
                confirmButton = {
                    Button(onClick = { showFullRollDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        // Name
        Text(
            text = student.name.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(0.45f)
                .clickable { showFullNameDialog = true }
        )

        Spacer(Modifier.weight(0.05f))

        // Present Button
        Box(
            modifier = Modifier
                .weight(0.1f)
                .aspectRatio(1f)
                .background(if (isPresent == 1) DarkGreen else Color(0xFFC8E6C9))
                .clickable {
                    if (isPresent == 1) {
                        isPresent = -1
                        onAttendanceMarked(-1)
                    } else {
                        isPresent = 1
                        onAttendanceMarked(1)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isPresent == 1) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Present",
                    tint = Color.White
                )
            }
        }

        // Absent Button
        Box(
            modifier = Modifier
                .weight(0.1f)
                .aspectRatio(1f)
                .background(if (isPresent == 0) DarkRed else Color(0xFFFFCDD2))
                .clickable {
                    if (isPresent == 0) {
                        isPresent = -1
                        onAttendanceMarked(-1)
                    } else {
                        isPresent = 0
                        onAttendanceMarked(0)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isPresent == 0) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Absent",
                    tint = Color.White
                )
            }
        }
    }

    // Full Name Dialog
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
