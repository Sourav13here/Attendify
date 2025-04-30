package com.example.attendify.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.example.attendify.ui.theme.PurpleLight
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
        contentDescriptionBackButton = "back button",
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        )
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please fill all the details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = PurpleLight, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                RadioButton(
                                    selected = accountType == "student",
                                    onClick = { accountType = "student" })
                                Text("Student", color = Color.Black)
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color.Black)
                            )

                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                RadioButton(
                                    selected = accountType == "teacher",
                                    onClick = { accountType = "teacher" })
                                Text("Teacher", color = Color.Black)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
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

                                if (accountType == "student") {
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

                            if (accountType == "student") {
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
                    CustomButton(text = "Register", action = {
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
                    })
                }
            }
            Spacer(modifier = Modifier.height(36.dp))
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

//@Preview(showSystemUi = true)
//@Composable
//fun Show_3() {
//    AttendifyTheme {
//        SignUp(rememberNavController())
//    }
//}
