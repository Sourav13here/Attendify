package com.example.attendify.ui.teacher.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.data.model.Student
import com.example.attendify.ui.teacher.TeacherDashboardViewModel
import com.example.attendify.ui.theme.CardColour
import com.example.attendify.ui.theme.CharcoalBlue
import com.example.attendify.ui.theme.DarkGreen
import com.example.attendify.ui.theme.DarkRed
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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
    var showDetailsDialog by remember { mutableStateOf(false) }

    var isPresent by remember { mutableIntStateOf(-1) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var pendingStatus by remember { mutableIntStateOf(-1) }

    val attendanceStatus by viewModel.attendanceStatusByEmail.collectAsState()
    val studentPercentages by viewModel.studentAttendanceInfo.collectAsState()

    val status = attendanceStatus[student.email]
    val percent = studentPercentages[student.email] ?: 0

    val context = LocalContext.current

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
            val maxVisibleChars = 12
            val rollNoDisplay = if (student.rollNumber.length > maxVisibleChars) {
                "…${student.rollNumber.takeLast(maxVisibleChars)}"
            } else {
                student.rollNumber
            }

            Text(
                text = rollNoDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.width(120.dp)
                    .clickable {
                        showDetailsDialog=true
                    },
                overflow = TextOverflow.Clip,
                softWrap = false
            )


            Text(
                text = student.name.uppercase(),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.45f)
                    .clickable { showDetailsDialog = true }
            )

            Spacer(Modifier.weight(0.05f))

            // Present Button
            Box(
                modifier = Modifier
                    .size(boxSize).clip(RoundedCornerShape(16.dp))
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
                    .size(boxSize).clip(RoundedCornerShape(16.dp))
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
                    }
                    ,
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

    // Name and Roll no dialogBox

    if (showDetailsDialog) {
        AlertDialog(
            onDismissRequest = { showDetailsDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = CharcoalBlue,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Student Details",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            text = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Full Name Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(12.dp).padding(end =2.dp)
                                    .offset(y =(-12).dp)
                            )
                            Column {
                                Text(
                                    text = "Full Name",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = student.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Divider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )

                        // Roll Number Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Numbers,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(14.dp)
                                    .offset(y =(-12).dp)
                            )
                            Column {
                                Text(
                                    text = "Roll Number",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = student.rollNumber,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDetailsDialog = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 6.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "Got it!",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        )
                    }
                }
            },
            containerColor = CardColour,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(16.dp)
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
