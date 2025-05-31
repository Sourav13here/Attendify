package com.example.attendify.ui.verification

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.LogoutButton
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.PrimaryColor

@Composable
fun VerificationStatus(
    navController: NavController,
    viewmodel: VerificationViewModel,
    userType: String,
    username: String,
    branch: String,
    semester: String?,
    roll: String?
) {
    val isLoading by viewmodel.isLoading.collectAsState()
    val context = LocalContext.current
    val navigateToStudentDashboard by viewmodel.navigateToStudentDashboard.collectAsState()
    val navigateToTeacherDashboard by viewmodel.navigateToTeacherDashboard.collectAsState()
    val userData by viewmodel.userData.collectAsState()
    val showEmailNotVerifiedBox by viewmodel.showEmailNotVerifiedBox.collectAsState()
    val snackbarMessage by viewmodel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewmodel.verifyAndSaveUser(
            userType = userType,
            username = username,
            branch = branch,
            semester = semester,
            roll = roll
        )
        viewmodel.refreshData()
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewmodel.clearSnackbar()
        }
    }

    LaunchedEffect(userData) {
        userData?.let {
            val name = when (it) {
                is UserData.StudentData -> it.student.name
                is UserData.TeacherData -> it.teacher.name
            }
            Toast.makeText(context, "Welcome $name", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(navigateToStudentDashboard) {
        if (navigateToStudentDashboard) {
            navController.navigate(NavRoutes.StudentDashboard.route) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(navigateToTeacherDashboard) {
        if (navigateToTeacherDashboard) {
            navController.navigate(NavRoutes.TeacherDashboard.route) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
        }
    }

    AppScaffold(
        title = "Verification Status",
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        actions = {
            LogoutButton(
                navController = navController,
                popUpToRoute = NavRoutes.VerificationStatus.route
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            userData?.let { data ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.8f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("YOUR DETAILS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        when (data) {
                            is UserData.StudentData -> {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Name: ")
                                        }
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                            append(data.student.name)
                                        }
                                    },
                                    fontSize = 16.sp
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Email: ")
                                        }
                                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                            append(data.student.email)
                                        }
                                    },
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Branch: ")
                                        }
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                            append(data.student.branch)
                                        }
                                    },
                                    fontSize = 16.sp
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Semester: ")
                                        }
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                            append(data.student.semester)
                                        }
                                    },
                                    fontSize = 16.sp
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Roll NO: ")
                                        }
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                            append(data.student.rollNumber)
                                        }
                                    },
                                    fontSize = 16.sp
                                )
                            }

                            is UserData.TeacherData -> {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Name: ")
                                        }
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                            append(data.teacher.name)
                                        }
                                    },
                                    fontSize = 16.sp
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Email: ")
                                        }
                                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                            append(data.teacher.email)
                                        }
                                    },
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("Branch: ")
                                        }
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                            append(data.teacher.branch)
                                        }
                                    },
                                    fontSize = 16.sp
                                )

                            }
                        }
                    }
                }
            } ?: Text("Loading your details...", fontSize = 16.sp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Always reserve a spot
                if (showEmailNotVerifiedBox) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(IntrinsicSize.Min), // adapt to content height
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Email not verified!!",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Please check your inbox and verify your email.",
                                color = Color.DarkGray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    // Invisible empty placeholder
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(90.dp)
                    ) // Same height roughly as your Card (adjust)
                } // Spacer outside is fine

                Card(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp) // Optional nice shadow
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp), // More breathing room
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Good spacing between items
                    ) {
                        Text(
                            text = "Your account is not verified yet.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Please wait for the admin to verify your account.",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = {
                                Log.e("verification", "clicked")
                                viewmodel.refreshData()
                                viewmodel.showSnackbar("Refreshing your status")
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLoading) Color.Gray else PrimaryColor
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.Black,
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Refresh", color = Color.White)
                            }
                        }
                    }

                }
            }
        }
    }
}
