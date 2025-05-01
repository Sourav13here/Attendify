package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.data.model.Student
import com.example.attendify.ui.teacher.TeacherDashboardViewModel

@Composable
fun StudentList(
    students: List<Student>,
    subjectName: String,
    markedBy: String,
    loading: Boolean,
    viewModel: TeacherDashboardViewModel,
    attendanceStatus: Map<String, Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF9999))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Roll No",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.width(120.dp)
            )

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp),
                color = Color.Black
            )

            Text(
                text = "Student Name",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .width(160.dp)
                    .padding(start = 8.dp)
            )

            Text(
                text = "P",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Green,
                modifier = Modifier
                    .width(30.dp)
                    .padding(start = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "A",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Red,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
        }

        // Content
        when {
            loading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            students.isEmpty() -> {
                Text(
                    text = "No students found.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
            }

            else -> {
                LazyColumn {
                    items(students) { student ->
                        StudentListItems(
                            rollNo = student.rollNumber,
                            studentName = student.name,
                            onAttendanceMarked = { status ->
                                if (status != -1) {
                                    viewModel.markAttendance(
                                        studentEmail = student.email,
                                        branch = student.branch,
                                        semester = student.semester,
                                        status = status,
                                        subjectName = subjectName,
                                        markedBy = markedBy
                                    )
                                }
                            },
                            attendanceStatus = attendanceStatus
                        )
                    }
                }
            }
        }
    }
}
