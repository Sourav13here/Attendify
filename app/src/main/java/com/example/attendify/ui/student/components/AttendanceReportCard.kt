package com.example.attendify.ui.student.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun AttendanceReportCard(
    totalClasses: Int,
    attendedClasses: Int,
    percentage: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .clickable { onClick() }, // Make clickable
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Assessment, contentDescription = "Attendance Report", tint = Color(0xFF673AB7))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Attendance Report", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AttendanceStatItem("Total Classes", totalClasses.toString())
                AttendanceStatItem("Classes Attended", attendedClasses.toString())
                AttendanceStatItem(
                    "Percentage", "$percentage%",
                    valueColor = when {
                        percentage >= 75 -> Color(0xFF4CAF50)
                        percentage >= 60 -> Color(0xFFFFA000)
                        else -> Color(0xFFF44336)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = percentage / 100f,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = when {
                    percentage >= 75 -> Color(0xFF4CAF50)
                    percentage >= 60 -> Color(0xFFFFA000)
                    else -> Color(0xFFF44336)
                },
                trackColor = Color.LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (percentage < 75) "Warning: Attendance below 75%" else "Good attendance!",
                color = if (percentage < 75) Color(0xFFF44336) else Color(0xFF4CAF50),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}



@Composable
fun AttendanceStatItem(
    title: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

