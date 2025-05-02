package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
    date: String,
    viewModel: TeacherDashboardViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(1.dp, Color.Gray)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF9999))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp),
                color = Color.Black
            )
            Text(
                text = "P",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .width(30.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "A",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp),
                color = Color.Black
            )
            Text(
                text = "%",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
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
                    itemsIndexed(students.sortedBy { it.rollNumber }) { index, student ->
                        StudentListItems(
                            index = index,
                            student = student,
                            onAttendanceMarked = { status ->
                                if (status != -1) {
                                    viewModel.markAttendance(
                                        studentEmail = student.email,
                                        branch = student.branch,
                                        semester = student.semester,
                                        status = status,
                                        subjectName = subjectName,
                                        markedBy = markedBy,
                                        date = date
                                    )
                                }
                            },
                            date = date,
                            subjectName = subjectName,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
