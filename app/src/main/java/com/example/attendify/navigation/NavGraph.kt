package com.example.attendify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendify.ui.login.Login
import com.example.attendify.ui.login.LoginViewModel
import com.example.attendify.ui.sign_up.SignUp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LoginPage.route
    ) {
        composable(NavRoutes.LoginPage.route) {
            Login(navController, loginViewModel)
        }
        composable(NavRoutes.SignUpPage.route) {
            SignUp(navController)
        }
    }
}
