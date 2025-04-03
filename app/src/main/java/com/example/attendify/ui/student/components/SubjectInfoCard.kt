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
fun SubjectInfoCard(subjectName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(0.85f),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "CS1809213",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Text(
                subjectName,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }

}
