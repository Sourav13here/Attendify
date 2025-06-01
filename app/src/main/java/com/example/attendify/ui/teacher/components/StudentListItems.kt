package com.example.attendify.ui.teacher.components

import android.annotation.SuppressLint
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StudentListItems(
    index: Int,
    student: Student,
    subjectName: String,
    onAttendanceMarked: (status: Int) -> Unit,
    viewModel: TeacherDashboardViewModel,
    selectedDate: String
) {
    var showFullNameDialog by remember { mutableStateOf(false) }
    var showFullRollDialog by remember { mutableStateOf(false) }
    var isPresent by remember { mutableIntStateOf(-1) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var pendingStatus by remember { mutableIntStateOf(-1) }

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

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val boxSize = maxWidth * 0.12f

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White)
                .padding(4.dp)
                .height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
                    .size(boxSize)
                    .background(if (isPresent == 1) DarkGreen else Color(0xFFC8E6C9))
                    .clickable {
                        val newStatus = if (isPresent == 1) -1 else 1
                        if (isPastDate(selectedDate)) {
                            pendingStatus = newStatus
                            showConfirmDialog = true
                        } else {
                            isPresent = newStatus
                            onAttendanceMarked(newStatus)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isPresent == 1) {
                    Icon(Icons.Filled.Check, contentDescription = "Present", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Absent Button
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(if (isPresent == 0) DarkRed else Color(0xFFFFCDD2))
                    .clickable {
                        val newStatus = if (isPresent == 0) -1 else 0
                        if (isPastDate(selectedDate)) {
                            pendingStatus = newStatus
                            showConfirmDialog = true
                            Log.e("selectedDate", "true items")
                        } else {
                            isPresent = newStatus
                            onAttendanceMarked(newStatus)
                            Log.e("selectedDate", "false items")

                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isPresent == 0) {
                    Icon(Icons.Filled.Close, contentDescription = "Absent", tint = Color.White)
                }
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
                Button(onClick = { showFullNameDialog = false }) { Text("OK") }
            }
        )
    }

    // Full Roll Dialog
    if (showFullRollDialog) {
        AlertDialog(
            onDismissRequest = { showFullRollDialog = false },
            title = { Text("Roll Number:") },
            text = { Text(student.rollNumber) },
            confirmButton = {
                Button(onClick = { showFullRollDialog = false }) { Text("OK") }
            }
        )
    }

    // Confirm Edit Dialog for Past Date
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Edit Past Attendance") },
            text = { Text("You are editing attendance for a previous date. Do you want to proceed?") },
            confirmButton = {
                Button(onClick = {
                    isPresent = pendingStatus
                    onAttendanceMarked(pendingStatus)
                    showConfirmDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showConfirmDialog = false
                    pendingStatus = -1
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun isPastDate(selectedDate: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Fix format here
        val selected = sdf.parse(selectedDate)

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        selected?.before(today) == true
    } catch (e: Exception) {
        Log.e("isPastDate", "Parse error: ${e.message}")
        false
    }
}
