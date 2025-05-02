package com.example.attendify.ui.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomIconButton
import com.example.attendify.data.model.Subject
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.teacher.components.AddSubjectPopup
import com.example.attendify.ui.teacher.components.VerifyFloatingButton
import com.example.attendify.ui.verification.components.LogoutConfirmationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun TeacherDashboard(
    navController: NavController,
    viewModel: TeacherDashboardViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showLogOutDialog by remember { mutableStateOf(false) }
    val subjects by viewModel.subjects.collectAsState()
    val teacher = viewModel.teacher.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadSubjects(teacher?.email ?: "")
    }

    if (showDialog) {
        AddSubjectPopup(
            onDismiss = { showDialog = false },
            onSubmit = { code, name, branch, semester ->
                viewModel.addSubject(
                    teacherEmail = teacher!!.email,
                    subject = Subject(
                        subjectCode = code,
                        subjectName = name,
                        createdBy = teacher?.email ?: "",
                        branch = branch,
                        semester = semester
                    ),
                    context = context
                )
            }
        )
    }

    if (showLogOutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.signOut()
                    navController.navigate(NavRoutes.LoginPage.route) {
                        popUpTo(NavRoutes.TeacherDashboard.route) { inclusive = true }
                    }
                }
                showLogOutDialog = false
            },
            onDismiss = { showLogOutDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppScaffold(
            title = "Teacher Dashboard",
            navController = navController,
            titleTextStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            actions = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFE57373), shape = CircleShape)
                ) {
                    CustomIconButton(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape),
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        onClick = { showLogOutDialog = true }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 80.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (teacher != null) {
                    Text(
                        text = "Welcome, " + teacher.name,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .padding(12.dp)
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color(0xFFD1B2E0), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (subjects.isEmpty()) {
                            Text("No subjects added yet.", style = MaterialTheme.typography.bodyMedium)
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
                                                "${NavRoutes.AttendanceSheet.route}/${subject.subjectCode}/${subject.subjectName}/${subject.branch}/${subject.semester}/${teacher.email}"
                                            )
                                        }
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
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // Make it clickable
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "$subjectCode - $subjectBranch ($subjectSem)", fontWeight = FontWeight.Bold)
            Text(text = subjectName)
        }
    }
}