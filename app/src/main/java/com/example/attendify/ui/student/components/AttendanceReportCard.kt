package com.example.attendify.ui.student.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AttendanceReportCard(totalClasses: Int, attendedClasses: Int, percentage: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(0.85f),

        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF))
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text("Total Class: ", fontSize = 14.sp)
                Text("$totalClasses", fontWeight = FontWeight.Bold)
            }
            Row {
                Text("Attended Class: ", fontSize = 14.sp)
                Text("$attendedClasses", fontWeight = FontWeight.Bold)
            }
            Row {
                Text("Percentage: ", fontSize = 14.sp)
                Text("$percentage%", fontWeight = FontWeight.Bold)
            }
        }
    }

}
