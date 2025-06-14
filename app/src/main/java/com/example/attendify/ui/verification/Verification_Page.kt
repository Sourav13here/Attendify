package com.example.attendify.ui.verification

import UnverifiedSummary
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.data.model.Student
import com.example.attendify.data.model.Teacher
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.GrayLight
import com.example.attendify.ui.theme.PrimaryVariant
import com.example.attendify.ui.theme.SurfaceColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Verification_Page(navController: NavController, viewModel: VerificationViewModel) {
    val students by viewModel.unverifiedStudents.collectAsState()
    val teachers by viewModel.unverifiedTeachers.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0 for students, 1 for teachers
    var selectedBranch by remember { mutableStateOf("Select Branch") }
    var selectedSemester by remember { mutableStateOf("Select Semester") }
    val isLoading by viewModel.isLoading.collectAsState()

    val branches = listOf("CSE", "ETE", "ME", "CE")
    val semesters = listOf("1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th")

    LaunchedEffect(selectedTab, selectedBranch, selectedSemester) {
        if (selectedBranch != "Select Branch" && (selectedTab == 1 || selectedSemester != "Select Semester")) {
            if (selectedTab == 0) {
                viewModel.fetchUnverifiedUsers("student", selectedBranch, selectedSemester)
            } else {
                viewModel.fetchUnverifiedUsers("teacher", selectedBranch)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.computeUnverifiedCounts()
    }

    LaunchedEffect(students, teachers) {
        println("Students: ${students.size} Teachers: ${teachers.size}")
    }

    val tabWidthPx = with(LocalDensity.current) { 120.dp.toPx() }
    val maxOffsetPx = tabWidthPx
    var dragOffsetPx by remember { mutableStateOf(0f) }

    // Calculate the target offset based on selectedTab and dragOffset
    val targetOffsetPx = if (selectedTab == 0) 0f else maxOffsetPx
    val offsetPx = targetOffsetPx + dragOffsetPx

    // Animate the sliding box offset but account for drag offset separately
    val animatedOffsetPx by animateFloatAsState(
        targetValue = if (selectedTab == 0) 0f else tabWidthPx,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    fun onTabSelected(index: Int) {
        selectedTab = index
    }

    AppScaffold(
        title = "Verification List",
        navController = navController,
        showLogo = false,
        contentDescriptionLogo = "App logo",
        showBackButton = true,
        contentDescriptionBackButton = "Back button",
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // SWITCH
            Box(
                modifier = Modifier
                    .width(120.dp * 2)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PrimaryVariant)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { dragOffsetPx = 0f },
                            onDrag = { change, dragAmount ->
                                dragOffsetPx =
                                    (dragOffsetPx + dragAmount.x).coerceIn(-tabWidthPx, tabWidthPx)
                                change.consume()
                            },
                            onDragEnd = {
                                val finalOffset = animatedOffsetPx + dragOffsetPx
                                val newSelectedTab = if (finalOffset < tabWidthPx / 2f) 0 else 1
                                onTabSelected(newSelectedTab)
                                dragOffsetPx = 0f
                            },
                            onDragCancel = {
                                dragOffsetPx = 0f
                            }
                        )
                    }
            ) {
                // Sliding white background that moves (animated + dragged)
                Box(
                    modifier = Modifier
                        .offset { IntOffset((animatedOffsetPx + dragOffsetPx).roundToInt(), 0) }
                        .width(120.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            2.dp, Color.Black,
                            RoundedCornerShape(20.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Students", "Teachers").forEachIndexed { index, label ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { onTabSelected(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (selectedTab == index) Color.Black else Color.DarkGray.copy(0.6f),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // UNVERIFIED DATA SECTION
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                DropdownSelector(
                    label = selectedBranch,
                    items = branches,
                    onItemSelected = { selectedBranch = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedTab == 0) {
                    DropdownSelector(
                        label = selectedSemester,
                        items = semesters,
                        onItemSelected = { selectedSemester = it }
                    )
                }else{
                    Spacer(modifier = Modifier.height(44.dp))
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.2f))

                // Main content of the verification page
                when {
                    selectedBranch == "Select Branch" || (selectedTab == 0 && selectedSemester == "Select Semester") -> {
                        // Show unverified counts grid for both tabs
                        UnverifiedSummary(
                            viewModel = viewModel,
                            selectedTab = selectedTab,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    (selectedTab == 0 && students.isEmpty()) || (selectedTab == 1 && teachers.isEmpty()) -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No unverified users found here.")
                        }
                    }
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp)
                        }
                    }
                    else -> {
                        LazyColumn(modifier = Modifier.padding(vertical = 6.dp)) {
                            val list = if (selectedTab == 0) students else teachers

                            items(list) { user ->
                                when (user) {
                                    is Student -> {
                                        UserVerificationItem(
                                            name = user.name,
                                            email = user.email,
                                            rollNumber = user.rollNumber,
                                            onApprove = {
                                                viewModel.approveUser(user.uid, "student")
                                            },
                                            onReject = {
                                                viewModel.rejectUser(user.uid, "student")
                                            }
                                        )
                                    }
                                    is Teacher -> {
                                        UserVerificationItem(
                                            name = user.name,
                                            email = user.email,
                                            rollNumber = null,
                                            onApprove = {
                                                viewModel.approveUser(user.uid, "teacher")
                                            },
                                            onReject = {
                                                viewModel.rejectUser(user.uid, "teacher")
                                            }
                                        )
                                    }
                                }
                            }
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
                    text = "DONE",
                    modifier = Modifier.fillMaxWidth(0.5f),
                    action = { navController.navigate(NavRoutes.TeacherDashboard.route) }
                )
            }
        }
    }
}



@Composable
fun DropdownSelector(
    label: String, items: List<String>, onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFE0E0E0))
        .clickable { expanded = true }
        .padding(horizontal = 12.dp, vertical = 10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label, modifier = Modifier.weight(1f), maxLines = 1
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(max = 168.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(item) }, onClick = {
                    onItemSelected(item)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun UserVerificationItem(
    name: String, email: String, rollNumber: String? = null, // Optional for Teacher
    onApprove: () -> Unit, onReject: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
            .background(Color(0xFFD9D9D9))
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name, fontSize = 14.sp, fontWeight = FontWeight.Medium
                )

                rollNumber?.let {
                    Text(
                        text = "Roll No: $it", fontSize = 14.sp, color = Color.DarkGray
                    )
                }

                Text(
                    buildAnnotatedString {
                        append("Email: ")

                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(email)
                        }
                    }, fontSize = 14.sp, color = Color.DarkGray
                )
            }

            // Approve/Reject buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ApprovalButton(text = "Approve", color = Color(0xFF187C1A), onClick = {
                    onApprove()
                })
                ApprovalButton(text = "Reject", color = Color(0xFFCB0D0D), onClick = { onReject() })
            }
        }
    }
}

@Composable
private fun ApprovalButton(text: String, color: Color, onClick: () -> Unit) {
    var isClicked by remember { mutableStateOf(false) }

    LaunchedEffect(isClicked) {
        if (isClicked) {
            kotlinx.coroutines.delay(300) // Wait for 300ms
            isClicked = false // Reset the state after the delay
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color.copy(0.5f))
            .clickable {
                isClicked = true
                onClick()
            }
            .border(2.dp, Color.White, CircleShape)) {
            if (isClicked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(0.6f)
                        .background(color, CircleShape) // Solid circle inside
                )
            }
        }
        Text(
            text = text, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun SpecialMessage(
    message: String, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp) // Add padding around the Box
            .fillMaxWidth(0.8f) // Adjust width
            .wrapContentSize(Alignment.Center) // Center the content in the Box
    ) {
        // Ellipse container with spiky border
        Box(
            modifier = Modifier
                .background(
                    Color(0xFFF5F5F5), shape = RoundedCornerShape(50) // Elliptical background
                )
                .border(2.dp, Color.Black, shape = RoundedCornerShape(50)) // Apply spiky border
                .padding(20.dp) // Padding inside the box
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}