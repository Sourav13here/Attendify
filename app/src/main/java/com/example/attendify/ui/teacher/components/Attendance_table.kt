package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.attendify.ui.theme.AttendifyTheme

@Composable
fun AttendanceTable() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {

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
                maxLines = 1,
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
                    .padding(end = 4.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "A",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Red,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
        }

        //Student List
        LazyColumn {
            items(List(10) { it }) { index ->
                val rollNo = "2220100070${index + 1}"
                val studentName = "Student Name${index + 1}"

                AttendanceRow(rollNo, studentName)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AttendancetableScreenPreview() {
    AttendifyTheme {
       AttendanceTable()
    }
}