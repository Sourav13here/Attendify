package com.example.attendify.ui.verification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomIconButton
import com.example.attendify.ui.theme.AttendifyTheme

@Composable
fun VerificationStatus(navController: NavController) {
    val userName = "John Smith"
    val branch = "CSE"
    val semester = "6th sem"
    val roll = "222020100023"

    AppScaffold(
        title = "Verification Status",
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        actions = {
            CustomIconButton(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

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
                    Text(userName, fontSize = 18.sp)
                    Text("$branch - $semester", fontSize = 16.sp)
                    Text("Roll - $roll", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f),
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
                        onClick = { },
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

@Preview(showSystemUi = true)
@Composable
fun DisplayVerification() {
    AttendifyTheme {
        VerificationStatus(rememberNavController())
    }
}