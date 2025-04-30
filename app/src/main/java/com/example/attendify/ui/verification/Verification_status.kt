package com.example.attendify.ui.verification

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomIconButton
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.verification.components.LogoutConfirmationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    var showLogOutDialog by remember { mutableStateOf(false) }
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

    if (showLogOutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                CoroutineScope(Dispatchers.Main).launch {
                    viewmodel.signOut()
                    navController.navigate(NavRoutes.LoginPage.route) {
                        popUpTo(NavRoutes.VerificationStatus.route) { inclusive = true }
                    }
                }
                showLogOutDialog = false
            },
            onDismiss = {
                showLogOutDialog = false
            }
        )
    }

    AppScaffold(
        title = "Verification Status",
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        actions = {
            CustomIconButton(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { showLogOutDialog = true }
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
                        .fillMaxWidth(0.9f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("YOUR DETAILS", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        when (data) {
                            is UserData.StudentData -> {
                                Text("Name: ${data.student.name}", fontSize = 16.sp)
                                Text(
                                    text = "Email: ${data.student.email}",
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text("Branch: ${data.student.branch}", fontSize = 16.sp)
                                Text("Semester: ${data.student.semester}", fontSize = 16.sp)
                                Text("Roll No: ${data.student.rollNumber}", fontSize = 16.sp)
                            }

                            is UserData.TeacherData -> {
                                Text("Name: ${data.teacher.name}", fontSize = 16.sp)
                                Text(
                                    text = "Email: ${data.teacher.email}",
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text("Branch: ${data.teacher.branch}", fontSize = 16.sp)
                            }
                        }
                    }
                }
            } ?: Text("Loading your details...", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(20.dp))

            if (showEmailNotVerifiedBox) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Email not verified!!", color = Color.Black,fontWeight = FontWeight.Bold)
                        Text("Please check your inbox and verify your email.", color = Color.Black,fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(0.85f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your account is not verified yet.")
                    Text(
                        "Please wait for the admin to verify your account.",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            Log.e("verification", "clicked")
                            viewmodel.refreshData()
                            viewmodel.showSnackbar("Refreshing your status")
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoading) Color.Gray else Color(0xFFE57373)
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
                            Text("Refresh")
                        }
                    }
                }
            }
        }
    }
}
