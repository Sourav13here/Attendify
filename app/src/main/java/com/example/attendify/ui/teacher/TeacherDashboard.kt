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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomIconButton


@Composable
fun TeacherDashboard(navController: NavController) {

    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AppScaffold(
            title = "Teacher Dashboard",
            navController = navController,
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
                        onClick = {}
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
                        repeat(6) {
                            SubjectCard("CS1809213", "Computer Networks")
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

    if (showDialog) {
        AddSubjectPopup(onDismiss = { showDialog = false })
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
/*TODO:Check UI of the code since preview not working due to faulty navigation*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectPopup(onDismiss: () -> Unit) {
    val branches = listOf("CSE", "ME", "CIVIL", "ETE")
    val semesters = listOf("1st sem", "2nd sem", "3rd sem", "4th sem", "5th sem", "6th sem")
    var selectedBranch by remember { mutableStateOf(branches[0]) }
    var selectedSemester by remember { mutableStateOf(semesters[0]) }
    var subjectName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Subject") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = subjectName, onValueChange = { subjectName = it }, label = { Text("Subject Name") })
                ExposedDropdownMenuBox(expanded = true, onExpandedChange = {}) {
                    OutlinedTextField(
                        value = selectedBranch,
                        onValueChange = {},
                        label = { Text("Select Branch") },
                        readOnly = true
                    )
                    DropdownMenu(expanded = true, onDismissRequest = {}) {
                        branches.forEach { branch ->
                            DropdownMenuItem(text = { Text(branch) }, onClick = { selectedBranch = branch })
                        }
                    }
                }
                ExposedDropdownMenuBox(expanded = true, onExpandedChange = {}) {
                    OutlinedTextField(
                        value = selectedSemester,
                        onValueChange = {},
                        label = { Text("Select Semester") },
                        readOnly = true
                    )
                    DropdownMenu(expanded = true, onDismissRequest = {}) {
                        semesters.forEach { semester ->
                            DropdownMenuItem(text = { Text(semester) }, onClick = { selectedSemester = semester })
                        }
                    }
                }
            }
        }
    )
}
@Preview(showSystemUi = true)
@Composable
fun DisplayTeacherDashboard() {
    TeacherDashboard(rememberNavController())
}
//@Preview(showSystemUi = true)
//@Composable
//fun PreviewAddSubjectPopup() {
//    AddSubjectPopup(onDismiss = {})
//}