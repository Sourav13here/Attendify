package com.example.attendify.ui.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold

@Composable
fun StudentDashboard(navController: NavController) {
    AppScaffold(
        title = "Student Dashboard",
        navController = navController,
        showLogo = true,
        contentDescriptionLogo = "App logo"
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

        }
    }
}