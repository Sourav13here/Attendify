package com.example.attendify.ui.student.components

import android.text.format.DateUtils.isToday
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.ui.theme.AttendifyTheme
import java.time.LocalDate
import com.example.attendify.R
@Composable
fun CalendarView(
    attendanceMap: Map<String, Boolean>,
    initialDate: LocalDate
) {
    var currentDate by remember { mutableStateOf(initialDate) }
    val currentMonth = currentDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val currentYear = currentDate.year

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color(0xFFE8F5E9))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { currentDate = currentDate.minusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Previous Month")
        }

            Text("$currentMonth $currentYear", fontSize = 18.sp)

            IconButton(onClick = { currentDate = currentDate.plusMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Week Days Row
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            days.forEach { Text(it, fontSize = 14.sp) }
        }

        // Generate the calendar dynamically based on the current month
        val firstDayOfMonth = currentDate.withDayOfMonth(1)
        val startDay = firstDayOfMonth.dayOfWeek.value % 7  // Adjust Sunday as 0
        val totalDays = firstDayOfMonth.month.length(firstDayOfMonth.isLeapYear)

        val daysGrid = mutableListOf<List<String>>()
        var day = 1
        for (week in 0 until 6) {  // Max 6 weeks in a month
            val row = mutableListOf<String>()
            for (col in 0 until 7) {
                if (week == 0 && col < startDay || day > totalDays) {
                    row.add("")
                } else {
                    row.add(day.toString())
                    day++
                }
            }
            daysGrid.add(row)
        }

        // Display the days in a grid format
        daysGrid.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                week.forEach { day ->
                    val isToday = day == LocalDate.now().dayOfMonth.toString() &&
                            currentDate.month == LocalDate.now().month &&
                            currentDate.year == LocalDate.now().year
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = when (attendanceMap[day]) {
                                    true -> Color.Green
                                    false -> Color.Red
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .then(
                                if (isToday) Modifier
                                    .padding(2.dp)
                                    .border(2.dp, Color.Blue, RoundedCornerShape(4.dp))
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day, fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}


