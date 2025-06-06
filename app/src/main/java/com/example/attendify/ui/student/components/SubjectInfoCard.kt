package com.example.attendify.ui.student.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.ui.theme.CardColour
import com.example.attendify.ui.theme.SecondaryColor

@Composable
fun SubjectInfoCard(subjectName: String, subjectCode: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.75f).border(2.dp, SecondaryColor.copy(0.7f), RoundedCornerShape(16.dp))
            .background(CardColour, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = subjectName,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subjectCode,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
        )
        }
    }


