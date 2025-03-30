package com.example.attendify.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold

@Composable
fun Login(navController: NavController) {
    AppScaffold(
        title = "Login",
        navController = navController,
        showLogo = true,
        contentDescriptionLogo = "App logo"
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

        }
    }
}