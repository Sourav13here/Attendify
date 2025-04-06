package com.example.attendify.ui.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.data.model.Student
import com.example.attendify.ui.theme.AttendifyTheme

@Composable
fun Verification_Page(navController: NavController) {
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
            var selectedTab by remember { mutableStateOf(0) }
            var selectedBranch by remember { mutableStateOf("Select Branch") }
            var selectedSemester by remember { mutableStateOf("Select Semester") }

            val branches = listOf("CSE", "ETE", "ME", "CE")
            val semesters = listOf("1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th")

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
                        Text("Students", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 14.sp)
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
                        Text("Teachers", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                }
            }

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

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 6.dp)
                    ) {
                        items(6) { index ->
                            StudentVerificationItem(
                                student = Student(
                                    name = "Faruk Khan",
                                    rollNumber = "23232323323",
                                    email = "abc@gmail.com"
                                )
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomButton(
                    text = "Done",
                    modifier = Modifier.width(160.dp),
                    action = {}
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
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = Color.Black)
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
fun StudentVerificationItem(student: Student) {
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
                            .clickable { /* Handle approval */ }
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
                            .clickable { /* Handle rejection */ }
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

@Preview(showSystemUi = true)
@Composable
fun VerificationScreenPreview() {
    AttendifyTheme {
        Verification_Page(navController = rememberNavController())
    }
}
