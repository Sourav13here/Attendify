package com.example.attendify.ui.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.example.attendify.navigation.NavRoutes

@Composable
fun Verification_Page(navController: NavController, viewModel: VerificationViewModel) {
    val students by viewModel.unverifiedStudents.collectAsState()
    val teachers by viewModel.unverifiedTeachers.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0 for students, 1 for teachers
    var selectedBranch by remember { mutableStateOf("Select Branch") }
    var selectedSemester by remember { mutableStateOf("Select Semester") }

    val branches = listOf("CSE", "ETE", "ME", "CE")
    val semesters = listOf("1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th")

    LaunchedEffect(selectedTab, selectedBranch, selectedSemester) {
        if (selectedBranch != "Select Branch" &&
            (selectedTab == 1 || selectedSemester != "Select Semester")) {

            if (selectedTab == 0) {
                viewModel.fetchUnverifiedUsers("student", selectedBranch, selectedSemester)
            } else {
                viewModel.fetchUnverifiedUsers("teacher", selectedBranch)
            }
        }
    }

    LaunchedEffect(students, teachers) {
        println("Students: ${students.size} Teachers: ${teachers.size}")
    }


    AppScaffold(
        title = "VERIFICATION",
        navController = navController,
        showLogo = false,
        contentDescriptionLogo = "App logo",
        showBackButton = true,
        contentDescriptionBackButton = "Back button",
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        )
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color.White)
                .fillMaxSize()
        ) {
//            Tab Selector
            Box(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 12.dp)
                    .width(250.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(Color(0xFFEC8484), RoundedCornerShape(20.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Students",
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .align(Alignment.CenterVertically)
                            .background(Color.Black)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Teachers",
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
//          Main Content (Dropdowns and List)
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 12.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .padding(horizontal = 24.dp, vertical = 6.dp)
                    ) {
                        Text("Unverified", color = Color.Black)
                    }

                    // Select Branch Dropdown
                    DropdownSelector(
                        label = selectedBranch,
                        items = branches,
                        onItemSelected = { selectedBranch = it }
                    )

                    // Select Semester Dropdown (Only for Students)
                    if (selectedTab == 0) {
                        DropdownSelector(
                            label = selectedSemester,
                            items = semesters,
                            onItemSelected = { selectedSemester = it }
                        )
                    }

                    if (selectedBranch == "Select Branch" || (selectedTab == 0 && selectedSemester == "Select Semester")) {
                        Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.Center,) {
                            Text("Please select a branch${if (selectedTab == 0) " and semester" else ""} to view users.")
                        }
                    }
                    else {
                        if ((selectedTab == 0 && students.isEmpty()) || (selectedTab == 1 && teachers.isEmpty())) {
                            // Show message when list is empty
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No unverified users found.")
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 6.dp)
                            ) {
                                if (selectedTab == 0) {
                                    items(students) { student ->
                                        StudentVerificationItem(
                                            student = student,
                                            onApprove = { viewModel.approveUser(student.uid, "student") },
                                            onReject = { viewModel.rejectUser(student.uid, "student") }
                                        )
                                    }
                                } else {
                                    items(teachers) { teacher ->
                                        TeacherVerificationItem(
                                            teacher = teacher,
                                            onApprove = { viewModel.approveUser(teacher.uid, "teacher") },
                                            onReject = { viewModel.rejectUser(teacher.uid, "teacher") }
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
//            Done Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomButton(
                    text = "Verify",
                    modifier = Modifier.width(160.dp),
                    action = {navController.navigate(NavRoutes.TeacherDashboard.route)}
                )
            }
        }
    }
}

@Composable
fun DropdownSelector(label: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .height(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFEC8484))
            .clickable { isDropdownExpanded = true },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color.Black, fontSize = 14.sp)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = Color.Black
            )
        }
    }

    DropdownMenu(expanded = isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
        items.forEach { item ->
            DropdownMenuItem(text = { Text(item) }, onClick = {
                onItemSelected(item)
                isDropdownExpanded = false
            })
        }
    }
}

@Composable
fun StudentVerificationItem(student: Student, onApprove: () -> Unit, onReject: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
            .padding(3.dp)
            .background(Color(0xFFD9D9D9))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Student Info Section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Roll No: ${student.rollNumber}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = "Email: ${student.email}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            // Approve/Reject Buttons Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Approve Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                Color(0xFF4CAF50), // Green color for approve
                                CircleShape
                            )
                            .clickable { onApprove() }
                    )
                    Text(
                        text = "Approve",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Reject Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                Color(0xFFEC8484), // Light red/pink color for reject
                                CircleShape
                            )
                            .clickable { onReject() }
                    )
                    Text(
                        text = "Reject",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TeacherVerificationItem(
    teacher: Teacher,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
            .padding(3.dp)
            .background(Color(0xFFD9D9D9))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Student Info Section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = teacher.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Email: ${teacher.email}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

            }

            // Approve/Reject Buttons Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Approve Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                Color(0xFF4CAF50), // Green color for approve
                                CircleShape
                            )
                            .clickable { onApprove() }
                    )
                    Text(
                        text = "Approve",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Reject Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                Color(0xFFEC8484), // Light red/pink color for reject
                                CircleShape
                            )
                            .clickable { onReject() }
                    )
                    Text(
                        text = "Reject",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun VerificationScreenPreview() {
//    AttendifyTheme {
//        Verification_Page(navController = rememberNavController())
//    }
//}
