// AttendanceSheet.kt
package com.example.attendify.ui.teacher

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.ui.teacher.components.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AttendanceSheet(
    navController: NavController,
    subjectName: String,
    branch: String,
    semester: String,
    teacherEmail: String,
    viewModel: TeacherDashboardViewModel
) {
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(getCurrentMonthYear()) }
    val students by viewModel.students.collectAsState()
    val isLoadingStudentsList by viewModel.isLoadingStudentsList.collectAsState()
    val fullDate = getFormattedFullDate(selectedDate, currentMonth) ?: ""
    val listState = rememberLazyListState()

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadStudents(branch, semester)
    }

    LaunchedEffect(selectedDate, currentMonth) {
        if (fullDate.isNotEmpty()) {
            viewModel.loadAttendanceStatusForDate(fullDate, subjectName, branch, semester)
        }
    }

    // Scroll to selected date whenever it or month changes
    LaunchedEffect(currentMonth, selectedDate) {
        // Assuming getDaysInMonth(currentMonth) returns List<Int> of all days in the month
        val daysInMonth = getDaysInMonth(currentMonth)
        val dayInt = selectedDate.toIntOrNull() ?: 1
        val index = daysInMonth.indexOf(dayInt).takeIf { it >= 0 } ?: 0
        listState.animateScrollToItem(index)
    }

    AppScaffold(
        title = subjectName,
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
        showLogo = false,
        showBackButton = true,
        actions = {
            Box(modifier = Modifier.wrapContentSize()) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More Options")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Download Report", style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            expanded = false
                            // TODO: Add download report functionality
                        }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Month Navigation Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$selectedDate $currentMonth",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                IconButton(
                    onClick = {
                        val initialYear = calendar.get(Calendar.YEAR)
                        val initialMonth = calendar.get(Calendar.MONTH)
                        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                selectedDate = dayOfMonth.toString().padStart(2, '0')

                                val cal = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, year)
                                    set(Calendar.MONTH, month)
                                    set(Calendar.DAY_OF_MONTH, 1)
                                }
                                val formattedMonthYear =
                                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)
                                currentMonth = formattedMonthYear
                            },
                            initialYear,
                            initialMonth,
                            initialDay
                        ).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Open Calendar"
                    )
                }
            }

            ScrollableDateSelectionRow(
                selectedDate = selectedDate,
                currentMonth = currentMonth,
                onDateSelected = { newDate -> selectedDate = newDate },
                listState = listState
            )

            StudentList(
                students = students,
                subjectName = subjectName,
                markedBy = teacherEmail,
                loading = isLoadingStudentsList,
                viewModel = viewModel,
                date = fullDate
            )
        }
    }
}

