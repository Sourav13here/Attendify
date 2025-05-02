package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.attendify.R

@Composable
fun ScrollableDateSelectionRow(selectedDate: String, currentMonth: String, onDateSelected: (String) -> Unit) {
    val daysInMonth = getDaysInMonth(currentMonth)

    //Get today's full date (DD MMMM YYYY) for correct highlighting
    val todayFullDate = getCurrentFullDate() // Example: "04 April 2025"

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        items(daysInMonth) { date ->
            val dateString = String.format("%02d", date)  // Two-digit format
            val dayOfWeek = getDayOfWeek(date, currentMonth)

            //Construct the full date for each day in the row
            val fullDate = "$dateString $currentMonth"
            val isToday = fullDate == todayFullDate
            val isSelectedDate = dateString == selectedDate

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(width = 48.dp, height = 60.dp)
                    .border(
                        BorderStroke(2.dp, if (isToday) Color(0xFF4CAEFF) else Color.Transparent),
                        RoundedCornerShape(16.dp)
                    )
                    .background(
                        if (isSelectedDate) colorResource(R.color.lightBlue) else Color.Transparent,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onDateSelected(dateString) },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = dayOfWeek, fontSize = 12.sp, fontWeight = FontWeight.Light)
                    Text(text = dateString, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
fun getCurrentFullDate(): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
}

// Date & Month Utility Functions
fun getCurrentDate(): String = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

fun getCurrentMonthYear(): String = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

fun getPreviousMonth(currentMonth: String): String = changeMonth(currentMonth, -1)

fun getNextMonth(currentMonth: String): String = changeMonth(currentMonth, 1)

fun changeMonth(currentMonth: String, offset: Int): String {
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(currentMonth)!!
    calendar.add(Calendar.MONTH, offset)
    return SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
}

fun getDaysInMonth(monthYear: String): List<Int> {
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(monthYear)!!
    return (1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).toList()
}

fun getDayOfWeek(day: Int, monthYear: String): String {
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(monthYear)!!
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
}

fun getFormattedFullDate(selectedDate: String, currentMonth: String): String {
    val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = inputFormat.parse("$selectedDate $currentMonth")!!
    return outputFormat.format(date)
}
