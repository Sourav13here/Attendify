package com.example.attendify.ui.teacher

import VerifyFloatingButton
import android.R.style
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.RectangleShape

import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomIconButton
import com.example.attendify.common.composable.LogoutButton
import com.example.attendify.data.model.Subject
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.teacher.components.AddSubjectPopup
import com.example.attendify.ui.theme.CharcoalBlue
import com.example.attendify.ui.theme.PrimaryColor
import com.example.attendify.ui.theme.SecondaryColor
import com.example.attendify.ui.theme.TextPrimary


@Composable
fun TeacherDashboard(
    navController: NavController,
    viewModel: TeacherDashboardViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val subjects by viewModel.subjects.collectAsState()
    val teacher by viewModel.teacher.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchTeacherData()
    }

    if (showDialog) {
        AddSubjectPopup(
            onDismiss = { showDialog = false },
            onSubmit = { code, name, branch, semester ->
                teacher?.email?.let { email ->
                    viewModel.addSubject(
                        teacherEmail = email,
                        subject = Subject(
                            subjectCode = code,
                            subjectName = name,
                            createdBy = email,
                            branch = branch,
                            semester = semester
                        ),
                        context = context
                    )
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppScaffold(
            title = "Teacher Dashboard",
            navController = navController,
            titleTextStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            actions = {
                LogoutButton(
                    navController = navController,
                    popUpToRoute = NavRoutes.TeacherDashboard.route
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                if (teacher != null) {
                    Text(
                        text = buildAnnotatedString {
                            append("Welcome, ")
                            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                append(teacher!!.name)
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(3.dp, CharcoalBlue, RectangleShape)
                            .padding(12.dp),
                    )
                } else {
                    CircularProgressIndicator()
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    CustomButton(
                        text = "Add Subjects",
                        modifier = Modifier.padding(16.dp),
                        action = { showDialog = true }
                    )
                }

                // ✅ Scrollable Subject List Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(540.dp)
                        .background(SecondaryColor.copy(0.4f), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()), // Only this part scrolls
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (subjects.isEmpty()) {
                            Text(
                                "No subjects added yet.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            subjects.forEach { subject ->
                                if (teacher != null) {
                                    SubjectCard(
                                        subjectCode = subject.subjectCode,
                                        subjectName = subject.subjectName,
                                        subjectBranch = subject.branch,
                                        subjectSem = subject.semester,
                                        onClick = {
                                            navController.navigate(
                                                "${NavRoutes.AttendanceSheet.route}/${subject.subjectCode}/${subject.subjectName}/${subject.branch}/${subject.semester}/${teacher!!.email}"
                                            )
                                        },
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        VerifyFloatingButton(navController)
    }
}


@Composable
fun SubjectCard(
    subjectCode: String,
    subjectBranch: String,
    subjectSem: String,
    subjectName: String,
    onClick: () -> Unit,
    viewModel: TeacherDashboardViewModel,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(0.9f)
            ) {
                Text(text = "$subjectCode - $subjectBranch ($subjectSem)", fontWeight = FontWeight.Bold)
                Text(text = subjectName)
            }

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
//                            viewModel.deleteSubject(subject, context) {}
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Download Report", style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            expanded = false
//                            viewModel.downloadAttendanceReport(subject, context)
                        }
                    )
                }
            }
        }
    }
}
