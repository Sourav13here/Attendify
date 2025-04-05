package com.example.attendify.ui.splashscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel
) {
    val startDestination by viewModel.startDestination.collectAsState()

    // Trigger check on first load
    LaunchedEffect(Unit) {
        viewModel.checkLoggedInUser()
    }

    // Navigate when destination is set
    LaunchedEffect(startDestination) {
        startDestination?.let { route ->
            navController.navigate(route) {
                popUpTo(0) // clear backstack
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Authenticating...", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}
