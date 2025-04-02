package com.example.attendify.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendify.ui.login.Login
import com.example.attendify.ui.sign_up.SignUp

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.LoginPage.route,
        modifier = Modifier
    ) {
        composable(NavRoutes.LoginPage.route) {
            Login(navController)
        }
        composable(NavRoutes.SignUpPage.route) {
            SignUp(navController)
        }
    }
}
