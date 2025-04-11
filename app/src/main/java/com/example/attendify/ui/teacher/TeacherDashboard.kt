package com.example.attendify.ui.teacher

import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomIconButton
import com.example.attendify.data.model.Subject
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.verification.components.LogoutConfirmationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun TeacherDashboard(navController: NavController, viewModel: TeacherDashboardViewModel = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val subjects by viewModel.subjects.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSubjects()
    }

    subjects.forEach{
        SubjectCard(it.subjectCode,it.subjectName)
    }


    if (showDialog) {
        AddSubjectPopup(
            onDismiss = { showDialog = false },
            onSubmit = {code,name, branch, semester ->
                viewModel.addSubject(
                    Subject(
                        subjectCode = code,
                        subjectName = name,
                        branch = branch,
                        semester = semester
                    )
                )
            }
        )
    }



    //    Logout Panel
    var showLogOutDialog by remember { mutableStateOf(false) }
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
            onDismiss = {
                showLogOutDialog = false
            }
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AppScaffold(
            title = "Teacher Dashboard",
            navController = navController,
            titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
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
                    .padding(bottom = 80.dp) // Leaves space for FAB
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome, JOHN SMITH",
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )

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
                                SubjectCard(subject.subjectCode, subject.subjectName)
                            }
                        }
                    }
                }
            }
        }

        /*TODO: Add Badge for convenience [to know when ]*/
        FloatingActionButton(
            onClick = { /* Verify Action */ },
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)

                .padding(16.dp)
        ) {
            Text("Verify")
        }
    }
    }

@Composable
fun SubjectCard(subjectCode: String, subjectName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = subjectCode, fontWeight = FontWeight.Bold)
            Text(text = subjectName)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectPopup(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String) -> Unit // subjectCode, subjectName, branch, semester
) {
    val branches = listOf("CSE", "ME", "CIVIL", "ETE")
    val semesters = listOf("1st sem", "2nd sem", "3rd sem", "4th sem", "5th sem", "6th sem")

    var selectedBranch by remember { mutableStateOf(branches[0]) }
    var selectedSemester by remember { mutableStateOf(semesters[0]) }
    var subjectName by remember { mutableStateOf("") }
    var subjectCode by remember { mutableStateOf("") }

    var expandedBranch by remember { mutableStateOf(false) }
    var expandedSemester by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSubmit(subjectCode, subjectName, selectedBranch, selectedSemester)
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Subject") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = { Text("Subject Name") }
                )

                OutlinedTextField(
                    value = subjectCode,
                    onValueChange = { subjectCode = it },
                    label = { Text("Subject Code") }
                )

                ExposedDropdownMenuBox(
                    expanded = expandedBranch,
                    onExpandedChange = { expandedBranch = !expandedBranch }
                ) {
                    OutlinedTextField(
                        value = selectedBranch,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Branch") },
                        trailingIcon = {

                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBranch)
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expandedBranch,
                        onDismissRequest = { expandedBranch = false }
                    ) {
                        branches.forEach { branch ->
                            DropdownMenuItem(
                                text = { Text(branch) },
                                onClick = {
                                    selectedBranch = branch
                                    expandedBranch = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandedSemester,
                    onExpandedChange = { expandedSemester = !expandedSemester }
                ) {
                    OutlinedTextField(
                        value = selectedSemester,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Semester") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSemester)
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expandedSemester,
                        onDismissRequest = { expandedSemester = false }
                    ) {
                        semesters.forEach { semester ->
                            DropdownMenuItem(
                                text = { Text(semester) },
                                onClick = {
                                    selectedSemester = semester
                                    expandedSemester = false
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}


//@Preview(showSystemUi = true)
//@Composable
//fun DisplayTeacherDashboard() {
//    TeacherDashboard(rememberNavController())
//}
