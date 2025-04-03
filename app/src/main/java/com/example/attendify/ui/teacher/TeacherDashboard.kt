package com.example.attendify.ui.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomIconButton


@Composable
fun TeacherDashboard(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        AppScaffold(
            title = "Teacher Dashboard",
            navController = navController,
            actions = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFE57373), shape = CircleShape)
                ) {
                    CustomIconButton(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape),
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        onClick = {}
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 80.dp) // Leaves space for FAB
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome, JOHN SMITH",
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    CustomButton(
                        text = "Add Subjects",
                        modifier = Modifier.padding(16.dp),
                        action = { /* Add Subjects Action */ }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color(0xFFD1B2E0), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(6) {
                            SubjectCard("CS1809213", "Computer Networks")
                        }
                    }
                }
            }
        }

        /*TODO: Add Badge for convenience [to know when ]*/
        FloatingActionButton(
            onClick = { /* Verify Action */ },
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)

                .padding(16.dp)
        ) {
            Text("Verify")
        }
    }
    }

@Composable
fun SubjectCard(subjectCode: String, subjectName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = subjectCode, fontWeight = FontWeight.Bold)
            Text(text = subjectName)
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun DisplayTeacherDashboard() {
    TeacherDashboard(rememberNavController())
}
