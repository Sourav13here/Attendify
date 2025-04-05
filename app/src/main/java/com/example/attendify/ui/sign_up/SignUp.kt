package com.example.attendify.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.R
import com.example.attendify.common.composable.*
import com.example.attendify.common.ext.customOutlinedTextField
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.AttendifyTheme
import com.example.attendify.ui.theme.PurpleLight
import com.example.attendify.utils.Constants

@Composable
fun SignUp(navController: NavController, viewModel: SignUpViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("Student") }
    var branch by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var rollno by remember { mutableStateOf("") }
    val context = LocalContext.current

    val navigateData by viewModel.navigateBasedOnAccount.collectAsState()

    LaunchedEffect(navigateData) {
        navigateData?.let { (isVerified, type) ->
            if (isVerified) {
                if (type == "Student") {
                    navController.navigate(NavRoutes.StudentDashboard.route) {
                        popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(NavRoutes.TeacherDashboard.route) {
                        popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
                    }
                }
            } else {
                navController.navigate(NavRoutes.VerificationStatus.route) {
                    popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
                }
            }
            viewModel.resetNavigationState()
        }
    }

    AppScaffold(
        title = "Sign Up",
        navController = navController,
        showBackButton = true,
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(PurpleLight, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Please fill all the details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomOutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Full Name",
                            modifier = Modifier.customOutlinedTextField()
                        )

                        CustomOutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            modifier = Modifier.customOutlinedTextField()
                        )

                        CustomOutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            isPasswordField = true,
                            modifier = Modifier.customOutlinedTextField()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Select Account Type", color = Color.Black)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = accountType == "Student",
                                    onClick = { accountType = "Student" }
                                )
                                Text("Student")
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color.Black)
                            )
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = accountType == "Teacher",
                                    onClick = { accountType = "Teacher" }
                                )
                                Text("Teacher")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    CustomExposedDropdown(
                                        label = "Branch",
                                        options = Constants.BRANCHES,
                                        selectedOption = branch,
                                        onOptionSelected = { branch = it },
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (accountType == "Student") {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        CustomExposedDropdown(
                                            label = "Semester",
                                            options = Constants.SEMESTERS,
                                            selectedOption = semester,
                                            onOptionSelected = { semester = it },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                if (accountType == "Student") {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    CustomOutlinedTextField(
                                        value = rollno,
                                        onValueChange = { rollno = it },
                                        label = "Enter your ROLL NO.",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE6B89C))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account?", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    CustomButton(
                        text = "Login",
                        action = {
                            navController.navigate(NavRoutes.LoginPage.route) {
                                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
                            }
                        },
                        isLoadingIcon = false
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ShowSignUp() {
    val viewModel: SignUpViewModel = hiltViewModel()
    AttendifyTheme {
        SignUp(rememberNavController(), viewModel)
    }
}
