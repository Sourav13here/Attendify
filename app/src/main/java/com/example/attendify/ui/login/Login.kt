package com.example.attendify.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.R
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomOutlinedTextField
import com.example.attendify.ui.theme.AttendifyTheme

@Composable
fun Login(navController: NavController) {
    AppScaffold(
        title = "Login",
        navController = navController,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFF817777)), // Grayish background
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Logo
                        Image(
                            painter = painterResource(id = R.drawable.college_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                        )
                    }
                }


                Spacer(modifier = Modifier.weight(1f))

                // Sign up section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE6B89C))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don't have an account?", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = { /* Navigate to Sign up */ }) {
                        Text(
                            "Sign up",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 200.dp, start = 40.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email Input Field
                    var email = remember { "" }
                    CustomOutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Password Input Field
                    var password = remember { "" }
                    CustomOutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isPasswordField = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Forgot Password
                    TextButton(onClick = { /* Forgot password action */ }) {
                        Text("Forgot Password?", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Login Button
                    Button(
                        onClick = { /* Login action */ },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC8484)),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Login", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DisplayLogin() {
    AttendifyTheme {
        Login(rememberNavController())
    }
}