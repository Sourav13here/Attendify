package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AttendanceRow(rollNo: String, studentName: String) {
    var isPresent by remember { mutableStateOf(false) }
    var isAbsent by remember { mutableStateOf(false) }
    var showFullNameDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (rollNo.hashCode() % 2 == 0) Color(0xFFF5F5F5) else Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rollNo,
            fontSize = 14.sp,
            modifier = Modifier.width(120.dp)
        )

        Divider(color = Color.Gray, modifier = Modifier.width(1.dp).height(24.dp))

        // Clickable Student Name
        Text(
            text = studentName,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(160.dp)
                .padding(start = 8.dp)
                .clickable { showFullNameDialog = true }
        )

        //"P" Button (Toggle Selection)
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(if (isPresent) Color.Green else Color(0xFFC8E6C9)) // Dark green if selected, else light green
                .clickable {
                    if (isPresent) {
                        isPresent = false  // Deselect if already selected
                    } else {
                        isPresent = true
                        isAbsent = false  // Ensure "A" is deselected
                    }
                }
        )

        Spacer(modifier = Modifier.width(4.dp)) // Space between P and A

        //"A" Button (Toggle Selection)
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(if (isAbsent) Color.Red else Color(0xFFFFCDD2)) // Dark red if selected, else light red
                .clickable {
                    if (isAbsent) {
                        isAbsent = false  // Deselect if already selected
                    } else {
                        isAbsent = true
                        isPresent = false  // Ensure "P" is deselected
                    }
                }
        )
    }

    // Show Dialog when name is clicked
    if (showFullNameDialog) {
        AlertDialog(
            onDismissRequest = { showFullNameDialog = false },
            title = { Text("Full Name:") },
            text = { Text(studentName) },
            confirmButton = {
                Button(onClick = { showFullNameDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}