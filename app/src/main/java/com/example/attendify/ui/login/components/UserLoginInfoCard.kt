package com.example.attendify.ui.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomOutlinedTextField
import com.example.attendify.common.composable.CustomTextButton
import com.example.attendify.common.ext.customOutlinedTextField
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.login.LoginViewModel

@Composable
fun UserLoginInfoCard(viewModel: LoginViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val loginError by viewModel.loginError.collectAsState()
    val navigateToStudent by viewModel.navigateToStudentDashboard.collectAsState()
    val navigateToTeacher by viewModel.navigateToTeacherDashboard.collectAsState()
    val navigateToStatus by viewModel.navigateToStatus.collectAsState()


    // Handle navigation
    LaunchedEffect(navigateToStudent) {
        if (navigateToStudent) {
            navController.navigate(NavRoutes.StudentDashboard.route) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
            viewModel.resetNavigationState()
        }
    }
    LaunchedEffect(navigateToTeacher) {
        if (navigateToTeacher) {
            navController.navigate(NavRoutes.TeacherDashboard.route) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
            viewModel.resetNavigationState()
        }
    }
    LaunchedEffect(navigateToStatus) {
        if (navigateToStatus) {
            navController.navigate(NavRoutes.VerificationStatus.route) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
            viewModel.resetNavigationState()
        }
    }


    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .offset(y = (-50).dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomOutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.clearLoginError()
                },
                label = "Email",
                modifier = Modifier.customOutlinedTextField()
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomOutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.clearLoginError()
                },
                label = "Password",
                isPasswordField = true,
                modifier = Modifier.customOutlinedTextField(),
            )

            Spacer(modifier = Modifier.height(10.dp))

            CustomTextButton(
                text = "Forgot Password?",
                action = { showForgotPasswordDialog = true }
            )

            if (showForgotPasswordDialog) {
                ForgetPasswordDialog(
                    onDismiss = { showForgotPasswordDialog = false },
                    viewModel = viewModel
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (loginError != null) {
                Text(loginError!!, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
            }

            CustomButton(text = "Login", action = {
                viewModel.login(email.trim(), password)
            })
        }
    }
}
