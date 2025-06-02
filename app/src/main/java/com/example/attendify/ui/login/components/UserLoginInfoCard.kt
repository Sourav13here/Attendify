package com.example.attendify.ui.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.sharp.Email
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
import com.example.attendify.ui.theme.CardColour
import com.example.attendify.ui.theme.SecondaryColor
import com.example.attendify.ui.theme.TextOnPrimary

@Composable
fun UserLoginInfoCard(viewModel: LoginViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val loginError by viewModel.loginError.collectAsState()
    val navigateToStudent by viewModel.navigateToStudentDashboard.collectAsState()
    val navigateToTeacher by viewModel.navigateToTeacherDashboard.collectAsState()
    val navigateToStatus by viewModel.navigateToStatus.collectAsState()

    // Navigation handling
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
            .offset(y = (-130).dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardColour)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email field
            CustomOutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.clearLoginError()
                },
                label = "Email",
                modifier = Modifier.customOutlinedTextField(),
                leadingIcon =Icons.Outlined.Email
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Password field
            CustomOutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.clearLoginError()
                },
                label = "Password",
                isPasswordField = true,
                modifier = Modifier.customOutlinedTextField(),
                leadingIcon = Icons.Outlined.Password
            )

            // Forgot Password aligned to right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y=(-10).dp)
                    .padding(top = 0.dp, bottom = 2.dp, end = 6.dp), // moved slightly up
                horizontalArrangement = Arrangement.End
            ) {
                CustomTextButton(
                    text = "Forgot Password?",
                    action = { showForgotPasswordDialog = true },
                )
            }

            // Error message in fixed height box
            // Error message with reserved height and smooth fade-in
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp), // Reserve space
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = loginError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = loginError ?: "",
                        color = Color.Red,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            // Login button (static position)
            CustomButton(text = "Login", action = {
                viewModel.login(email.trim(), password)
            })

            // Forgot password dialog
            if (showForgotPasswordDialog) {
                ForgetPasswordDialog(
                    onDismiss = { showForgotPasswordDialog = false },
                    viewModel = viewModel
                )
            }
        }
    }
}
