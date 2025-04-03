package com.example.attendify.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.*
import com.example.attendify.ui.theme.AttendifyTheme
import com.example.attendify.ui.theme.GrayLight
import com.example.attendify.ui.theme.PurpleLight

@Composable
fun SignUp(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("Student") }

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
                    CustomOutlinedTextField(value = username, onValueChange = { username = it }, label = "Full Name", modifier = Modifier.fillMaxWidth())
                    CustomOutlinedTextField(value = email, onValueChange = { email = it }, label = "Email", modifier = Modifier.fillMaxWidth())
                    CustomOutlinedTextField(value = password, onValueChange = { password = it }, label = "Password", isPasswordField = true, modifier = Modifier.fillMaxWidth())
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
                                RadioButton(selected = accountType == "Student", onClick = { accountType = "Student" })
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
                                RadioButton(selected = accountType == "Teacher", onClick = { accountType = "Teacher" })
                                Text("Teacher", color = Color.Black)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                CustomExposedDropdown(label = "Branch", options = listOf("CSE", "ETE", "ME", "CE"), selectedOption = "CSE", onOptionSelected = {}, modifier = Modifier.weight(1f))

                                if (accountType == "Student") {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    CustomExposedDropdown(label = "Semester", options = listOf("1", "2", "3"), selectedOption = "1", onOptionSelected = {}, modifier = Modifier.weight(1f))
                                }
                            }

                            if (accountType == "Student") {
                                Spacer(modifier = Modifier.height(8.dp))
                                CustomOutlinedTextField(value = "", onValueChange = {}, label = "Enter your ROLL NO.", modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    CustomButton(text = "Register", action = {})
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
                    action = {},
                    isLoadingIcon = false
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Show_3() {
    AttendifyTheme {
        SignUp(rememberNavController())
    }
}
