package com.example.attendify.ui.teacher

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.data.model.Subject
import com.example.attendify.ui.teacher.components.ScrollableDateSelectionRow
import com.example.attendify.ui.teacher.components.StudentList
import com.example.attendify.ui.teacher.components.getCurrentDate
import com.example.attendify.ui.teacher.components.getCurrentMonthYear
import com.example.attendify.ui.teacher.components.getDaysInMonth
import com.example.attendify.ui.teacher.components.getFormattedFullDate
import com.example.attendify.ui.theme.SecondaryColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    var showDialog by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var currentMonth by remember { mutableStateOf(getCurrentMonthYear()) }
    val students by viewModel.students.collectAsState()
    val isLoadingStudentsList by viewModel.isLoadingStudentsList.collectAsState()
    val fullDate = getFormattedFullDate(selectedDate, currentMonth) ?: ""
    val listState = rememberLazyListState()
    Log.e("dateSelected", "$fullDate attendancesheet")

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
                IconButton(onClick = {
                    showDialog = true // Show the dialog on click
                }) {
                    Icon(Icons.Filled.Download, contentDescription = "Download report")
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = {
                            Text(text = "Download Report",style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                        },
                        text = {
                            Text("Do you want to download the attendance report in .xlsx format?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    val subject = Subject(
                                        subjectName = subjectName,
                                        branch = branch,
                                        semester = semester
                                    )
                                    viewModel.downloadAttendanceReport(subject, students, context)
                                }
                            ) {
                                Text("Yes",style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("Cancel", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            }
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
                modifier = Modifier.offset(x =(10).dp)
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
                                    SimpleDateFormat(
                                        "MMMM yyyy",
                                        Locale.getDefault()
                                    ).format(cal.time)
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
                        contentDescription = "Open Calendar",
                        tint = SecondaryColor
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
