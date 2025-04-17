package com.example.attendify.ui.verification

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val navigateToStudentDashboard by viewmodel.navigateToStudentDashboard.collectAsState()
    val navigateToTeacherDashboard by viewmodel.navigateToTeacherDashboard.collectAsState()

    var showLogOutDialog by remember { mutableStateOf(false) }

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
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        actions = {
            CustomIconButton(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = {
                    showLogOutDialog = true
                }
            )
        }
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("YOUR DETAILS", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = username, fontSize = 18.sp)
                    Text(text = branch, fontSize = 16.sp)
                    // Show roll number only if it's not null or "null" string
                    if (!roll.isNullOrEmpty() && roll != "null" && userType == "student") {
                        Text(text = "$semester", fontSize = 16.sp)
                        Text(text = "Roll - $roll", fontSize = 16.sp)
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

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Refresh")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                }
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun DisplayVerification() {
//    val viewModel: VerificationViewModel = hiltViewModel()
//    AttendifyTheme {
//        VerificationStatus(
//            navController = rememberNavController(),
//            viewModel
//        )
//    }
//}
