package com.example.attendify.ui.teacher

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.ui.theme.AttendifyTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AttendanceSheet(navController: NavController) {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(getCurrentMonthYear()) }

    AppScaffold(
        title = "Computer Networks",
        navController = navController,
        showLogo = false,
        showBackButton = true
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Subject Box
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "CS1809213", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Computer Networks", fontSize = 14.sp)
                }
            }

            // Month Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentMonth = getPreviousMonth(currentMonth)
                    selectedDate = "01" // Reset to 1st of new month
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Month")
                }
                Text(text = currentMonth, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = {
                    currentMonth = getNextMonth(currentMonth)
                    selectedDate = "01" // Reset to 1st of new month
                }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Month")
                }
            }

            // Scrollable Date Selection
            DateSelectionRow(selectedDate, currentMonth) { newDate ->
                selectedDate = newDate
            }

            // Attendance Table
            AttendanceTable()
        }
    }
}

//Scrollable Date Row with Day Names
@Composable
fun DateSelectionRow(selectedDate: String, currentMonth: String, onDateSelected: (String) -> Unit) {
    val daysInMonth = getDaysInMonth(currentMonth)

    //Get today's full date (DD MMMM YYYY) for correct highlighting
    val todayFullDate = getCurrentFullDate() // Example: "04 April 2025"

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(daysInMonth) { date ->
            val dateString = String.format("%02d", date)  // Two-digit format
            val dayOfWeek = getDayOfWeek(date, currentMonth)

            // ✅ Construct the full date for each day in the row
            val fullDate = "$dateString $currentMonth"  // Example: "04 April 2025"
            val isToday = fullDate == todayFullDate   // ✅ Check if it matches today

            val isSelectedDate = dateString == selectedDate

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(60.dp, 70.dp)
                    .border(
                        BorderStroke(2.dp, if (isToday) Color.Blue else Color.Transparent), // ✅ Only highlight real current date
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        if (isSelectedDate) Color(0xFF4CAEFF) else Color.Transparent,
                        RoundedCornerShape(4.dp)
                    )
                    .clickable { onDateSelected(dateString) },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = dayOfWeek, fontSize = 12.sp, fontWeight = FontWeight.Light)
                    Text(text = dateString, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
fun getCurrentFullDate(): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
}


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

        Divider(
            color = Color.Gray,
            modifier = Modifier
                .width(1.dp)
                .height(24.dp)
        )

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


@Preview(showSystemUi = true)
@Composable
fun AttendanceSheetScreenPreview() {
    AttendifyTheme {
        AttendanceSheet(navController = rememberNavController())
    }
}
