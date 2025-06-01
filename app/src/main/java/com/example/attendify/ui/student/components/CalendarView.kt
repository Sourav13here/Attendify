package com.example.attendify.ui.student.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.ui.student.StudentDashboardViewModel
import com.example.attendify.ui.theme.PrimaryVariant
import java.time.LocalDate

@Composable
fun CalendarView(
    attendanceMap: Map<String, Int>, // Firestore data: "yyyy-MM-dd" -> 1 or 0
    initialDate: LocalDate,
    localAttendanceMap: MutableMap<String, Int>, // Temporary attendance changes
    viewmodel: StudentDashboardViewModel
) {
    var currentDate by remember { mutableStateOf(initialDate) }
    val currentMonth = currentDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val currentYear = currentDate.year


    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)

            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryVariant.copy(0.2f))

            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header: Month + Navigation
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

        // Weekday headers
        val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            weekdays.forEach {
                Text(text = it, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Calendar generation
        val firstDayOfMonth = currentDate.withDayOfMonth(1)
        val startDayOffset = (firstDayOfMonth.dayOfWeek.value + 6) % 7 // To make Monday first
        val totalDaysInMonth = currentDate.month.length(firstDayOfMonth.isLeapYear)

        val daysGrid = mutableListOf<List<String>>()
        var day = 1
        for (week in 0 until 6) {
            val row = mutableListOf<String>()
            for (col in 0 until 7) {
                if (week == 0 && col < startDayOffset || day > totalDaysInMonth) {
                    row.add("")
                } else {
                    row.add(day.toString())
                    day++
                }
            }
            daysGrid.add(row)
        }

        // Grid UI
        daysGrid.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { dayStr ->
                    val fullDate = if (dayStr.isNotEmpty()) {
                        LocalDate.of(currentDate.year, currentDate.month, dayStr.toInt()).toString()
                    } else ""

                    // Prioritize local attendance changes over Firestore data
                    val attendanceStatus = if (fullDate.isNotEmpty()) {
                        localAttendanceMap[fullDate] ?: attendanceMap[fullDate] ?: -1
                    } else -1
                    val isToday = fullDate == LocalDate.now().toString()

                    // Check if the date is today or in the future
                    val dateObj = if (fullDate.isNotEmpty()) LocalDate.parse(fullDate) else null
                    val isFutureOrToday = dateObj != null && !dateObj.isBefore(LocalDate.now())

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = when (attendanceStatus) {
                                    1 -> Color(0xFF81C784) // Green
                                    0 -> Color(0xFFE57373) // Red
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = if (isToday) 2.dp else 0.dp,
                                color = if (isToday) Color(0xFF64B5F6) else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable(
                                enabled = fullDate.isNotEmpty() && isFutureOrToday && (localAttendanceMap[fullDate] != null || (attendanceMap[fullDate] == null && localAttendanceMap[fullDate] == null))
                            ) {

                                val currentStatus = localAttendanceMap[fullDate]
                                when (currentStatus) {
                                    1 -> localAttendanceMap[fullDate] = 0  // Present → Absent
                                    0 -> localAttendanceMap.remove(fullDate)  // Absent → Remove entry
                                    else -> localAttendanceMap[fullDate] = 1
                                }
                                Log.d(
                                    "CalendarClick",
                                    "Clicked $fullDate, Now = ${localAttendanceMap[fullDate]}"
                                )


                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayStr,
                            fontSize = 12.sp,
                            color = if (!isFutureOrToday && fullDate.isNotEmpty()) Color(0xFF8D6E63) else Color.Black
                        )
                    }
                }
            }
        }
    }
}






