// components/ScrollableDateSelectionRow.kt
package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.R
import com.example.attendify.ui.theme.SecondaryColor
import com.example.attendify.ui.theme.SurfaceColor
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScrollableDateSelectionRow(
    selectedDate: String,
    currentMonth: String,
    onDateSelected: (String) -> Unit,
    listState: LazyListState
) {
    val daysInMonth = getDaysInMonth(currentMonth)
    val todayFullDate = getCurrentFullDate()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 3

    val todayIndex = daysInMonth.indexOfFirst {
        val fullDate = String.format("%02d", it) + " " + currentMonth
        fullDate == todayFullDate
    }

    LaunchedEffect(currentMonth) {
        if (todayIndex != -1) {
            val centerIndex = (todayIndex - 1).coerceAtLeast(0)
            val maxIndex = daysInMonth.size - 3
            val scrollToIndex = centerIndex.coerceAtMost(maxIndex)
            listState.scrollToItem(scrollToIndex)
        }
    }

    val totalItems = daysInMonth.size
    val visibleItems = listState.layoutInfo.visibleItemsInfo.size
    val firstIndex = listState.firstVisibleItemIndex
    val scrollBarWidth = screenWidth / (totalItems / visibleItems.toFloat()).coerceAtLeast(1f)

    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState
        ) {
            items(daysInMonth) { date ->
                val dateString = String.format("%02d", date)
                val dayOfWeek = getDayOfWeek(date, currentMonth)
                val fullDate = "$dateString $currentMonth"
                val isToday = fullDate == todayFullDate
                val isSelectedDate = dateString == selectedDate

                Box(
                    modifier = Modifier
                        .width(itemWidth)
                        .padding(4.dp)
                        .border(
                            BorderStroke(2.dp, if (isToday) SecondaryColor else Color.Transparent),
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            if (isSelectedDate) SurfaceColor.copy(0.4f) else Color.Transparent,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onDateSelected(dateString) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = dayOfWeek,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = if (isSelectedDate) Color.Black else Color.Gray
                        )
                        Text(
                            text = dateString,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelectedDate) Color.Black else Color.Gray
                        )
                    }
                }
            }
        }

        // Scrollbar under LazyRow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        val fraction = if (totalItems > visibleItems) {
                            (firstIndex / (totalItems - visibleItems).toFloat()).coerceIn(0f, 1f)
                        } else 0f
                        IntOffset((fraction * (screenWidth - scrollBarWidth).toPx()).toInt(), 0)
                    }
                    .width(scrollBarWidth)
                    .fillMaxHeight()
                    .background(Color.Blue, RoundedCornerShape(4.dp))
            )
        }
    }
}


fun getCurrentFullDate(): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
}

fun getCurrentDate(): String = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

fun getCurrentMonthYear(): String =
    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

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

fun getFormattedFullDate(selectedDate: String, currentMonth: String): String? {
    val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse("$selectedDate $currentMonth") ?: return null
    return outputFormat.format(date)
}
