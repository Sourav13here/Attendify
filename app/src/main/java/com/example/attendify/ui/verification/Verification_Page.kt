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
            // Students/Teachers Tab - Made smaller
            var selectedTab by remember { mutableStateOf(0) }

            Box(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 12.dp)
                    .width(250.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                // Tab background with rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFEC8484))
                )

                // Red circles at corners
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 4.dp, y = 4.dp)
                        .background(Color.Red, CircleShape)
                )

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp)
                        .background(Color.Red, CircleShape)
                )

                // Tab content with divider
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
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

                    // Vertical Divider between tabs
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
                            .fillMaxHeight()
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

            // Black border container that wraps everything except the Done button
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
            ) {
                Column {
                    // Unverified Status
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
                    Box(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEC8484))
                            .clickable { },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Select Branch", color = Color.Black, fontSize = 14.sp)
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Black
                            )
                        }
                    }

                    // Select Semester Dropdown
                    Box(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEC8484))
                            .clickable { },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Select Semester", color = Color.Black, fontSize = 14.sp)
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Black
                            )
                        }
                    }

                    // Student List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 6.dp)
                    ) {
                        items(6) { index ->
                            StudentVerificationItem(
                                student = Student(
                                    name = "Faruk Khan",
                                    rollNo = "23232323323",
                                    email = "abc@gmail.com"
                                ),
                                isApproved = index == 0
                            )
                        }
                    }
                }
            }

            // Done Button - outside the black border
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
fun StudentVerificationItem(
    student: Student,
    isApproved: Boolean = false
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
            // Student info section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Roll No: ${student.rollNo}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = "Email: ${student.email}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            // Approve/Reject buttons section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Approve button
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
            }

            // Space between buttons
            Spacer(modifier = Modifier.width(16.dp))

            // Reject button
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

data class Student(
    val name: String,
    val rollNo: String,
    val email: String
)

@Preview(showSystemUi = true)
@Composable
fun VerificationScreenPreview() {
    AttendifyTheme {
        Verification_Page(navController = rememberNavController())
    }
}