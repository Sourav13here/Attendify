package com.example.attendify.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomExposedDropdown
import com.example.attendify.common.composable.CustomOutlinedTextField
import com.example.attendify.common.ext.customOutlinedTextField
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.BackgroundColor
import com.example.attendify.ui.theme.SecondaryColor
import com.example.attendify.utils.Constants

@Composable
fun SignUp(navController: NavController, viewModel: SignUpViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("student") }
    var branch by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var rollno by remember { mutableStateOf("") }
    val context = LocalContext.current

    val navigateToVerification by viewModel.navigateToVerification.collectAsState()

    LaunchedEffect(navigateToVerification) {
        if (navigateToVerification) {
            navController.navigate(
                NavRoutes.VerificationStatus.createRoute(
                    userType = accountType.lowercase(),
                    username = username,
                    branch = branch,
                    semester = semester,
                    roll = rollno,
                )
            ) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
            viewModel.resetNavigationState()
        }
    }

    AppScaffold(
        title = "SIGN UP",
        navController = navController,
        showBackButton = true,
        contentDescriptionBackButton = "Back",
        titleTextStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 20.dp)
        ) {
            val maxWidth = maxWidth

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 420.dp)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top content
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Enter details",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundColor, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CustomOutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Full Name",
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Outlined.Person2
                        )
                        CustomOutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Outlined.Email
                        )
                        CustomOutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            isPasswordField = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Outlined.Password
                        )

                        Spacer(Modifier.heightIn(min = 50.dp, max = 160.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf("Student", "Teacher").forEach { type ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = accountType == type.lowercase(),
                                            onClick = { accountType = type.lowercase() }
                                        )
                                        Text(type, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomExposedDropdown(
                                label = "Branch",
                                options = Constants.BRANCHES,
                                selectedOption = branch,
                                onOptionSelected = { branch = it },
                                modifier = Modifier.weight(1f)
                            )
                            if (accountType == "student") {
                                CustomExposedDropdown(
                                    label = "Semester",
                                    options = Constants.SEMESTERS,
                                    selectedOption = semester,
                                    onOptionSelected = { semester = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        if (accountType == "student") {
                            CustomOutlinedTextField(
                                value = rollno,
                                onValueChange = { rollno = it },
                                label = "Roll No.",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CustomButton(
                                text = "Register",
                                action = {
                                    viewModel.createAccount(
                                        context,
                                        username,
                                        email,
                                        password,
                                        accountType,
                                        branch,
                                        semester,
                                        rollno
                                    )
                                }
                            )
                        }
                    }
                }

                // Bottom static button
                TextButton(
                    onClick = {
                        navController.navigate(NavRoutes.LoginPage.route) {
                            popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
                        }
                    }
                ) {
                    Text("Already have an account? Login", fontSize = 13.sp)
                }
            }
        }
    }
}