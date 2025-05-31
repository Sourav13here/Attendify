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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.data.model.Subject
import com.example.attendify.ui.teacher.components.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AttendanceSheet(
    navController: NavController,
    subjectCode: String,
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

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(currentMonth, selectedDate) {
        val daysInMonth = getDaysInMonth(currentMonth)
        val dayInt = selectedDate.toIntOrNull() ?: 1
        val index = daysInMonth.indexOf(dayInt).takeIf { it >= 0 } ?: 0

        // Convert screen width dp to pixels
        val screenWidthDp = configuration.screenWidthDp
        val screenWidthPx = with(density) { screenWidthDp.dp.toPx() }
        val itemWidthPx = screenWidthPx / 3 // same as ScrollableDateSelectionRow
        val centerOffset = (screenWidthPx / 2) - (itemWidthPx / 2)

        listState.animateScrollToItem(index, scrollOffset = -centerOffset.toInt())
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
                        text = { Text("Delete Subject", style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            expanded = false
                            val subject = Subject(subjectCode = subjectCode, subjectName = subjectName, branch = branch, semester = semester)
                            viewModel.deleteSubject(subject, context)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Download Report", style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            expanded = false
                            val subject = Subject(subjectName = subjectName, branch = branch, semester = semester)
                            viewModel.downloadAttendanceReport(subject, context)
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

// Helper functions you need to implement or already have:
// getCurrentDate() -> String (e.g. "01")
// getCurrentMonthYear() -> String (e.g. "May 2025")
// getFormattedFullDate(day: String, monthYear: String) -> String? (e.g. "2025-05-01")
// getDaysInMonth(monthYear: String) -> List<Int> (days in the current month)
