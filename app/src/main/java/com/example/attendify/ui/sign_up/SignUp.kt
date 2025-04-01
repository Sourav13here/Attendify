package com.example.attendify.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomExposedDropdown
import com.example.attendify.common.composable.CustomOutlinedTextField
import com.example.attendify.common.composable.CustomTextButton
import com.example.attendify.ui.theme.AttendifyTheme

@Composable
fun SignUp(navController: NavController) {

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    CustomOutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Full Name",

                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomOutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomOutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isPasswordField = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select Account Type")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = true, onClick = {})
                        Text("Student")
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(selected = false, onClick = {})
                        Text("Teacher")
                    }

                    Row {
                        CustomExposedDropdown(
                            label = "Branch",
                            options = listOf("CSE", "ETE", "ME","CE"),
                            selectedOption = "CSE",
                            onOptionSelected = {}
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomExposedDropdown(
                            label = "Semester",
                            options = listOf("1", "2", "3"),
                            selectedOption = "1",
                            onOptionSelected = {}
                        )
                    }
                    CustomOutlinedTextField(value = "", onValueChange = {}, label = "Enter your ROLL NO.")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(text = "Register", action = {}, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text("Already have an account?")
                Spacer(modifier = Modifier.width(4.dp))
                CustomTextButton(text = "Login", modifier = Modifier, action = {})
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun show_3() {
    AttendifyTheme {
        SignUp(rememberNavController())
    }
}
