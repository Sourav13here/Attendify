package com.example.attendify.ui.teacher.components

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.data.model.Student
import com.example.attendify.ui.teacher.TeacherDashboardViewModel
import java.util.Locale

@Composable
fun StudentListItems(
    index: Int,
    student: Student,
    subjectName: String,
    onAttendanceMarked: (status: Int) -> Unit,
    viewModel: TeacherDashboardViewModel
) {
    var showFullNameDialog by remember { mutableStateOf(false) }

    var isPresent by remember { mutableIntStateOf(-1) }
    val attendanceStatus by viewModel.attendanceStatusByEmail.collectAsState()
    val status = attendanceStatus[student.email]
    val studentPercentages by viewModel.studentAttendanceInfo.collectAsState()
    val percent = studentPercentages[student.email] ?: 0
    var showFullRollDialog by remember { mutableStateOf(false) }

    LaunchedEffect(status) {
        if (status != null) {
            isPresent = status
        } else {
            isPresent = -1
        }
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
                .background(if (isPresent == 1 || status == 1) Color.Green else Color(0xFFC8E6C9))
                .clickable() {
                    isPresent = 1
                    onAttendanceMarked(1)
                }
        )

        // Absent Button
        Box(
            modifier = Modifier
                .weight(0.1f)
                .aspectRatio(1f)
                .background(if (isPresent == 0 || status == 0) Color.Red else Color(0xFFFFCDD2))
                .clickable() {
                    isPresent = 0
                    onAttendanceMarked(0)
                }
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
