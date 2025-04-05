package com.example.attendify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendify.ui.login.Login
import com.example.attendify.ui.login.LoginViewModel
import com.example.attendify.ui.sign_up.SignUp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.attendify.ui.sign_up.SignUpViewModel
import com.example.attendify.ui.student.StudentDashboard
import com.example.attendify.ui.teacher.TeacherDashboard
import com.example.attendify.ui.verification.VerificationStatus

@Composable
fun NavGraph(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val signUpViewModel: SignUpViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LoginPage.route
    ) {
        composable(NavRoutes.LoginPage.route) {
            Login(navController, loginViewModel)
        }
        composable(NavRoutes.SignUpPage.route) {
            SignUp(navController, signUpViewModel)
        }
        composable(NavRoutes.VerificationStatus.route) {
            VerificationStatus(navController)
        }
        composable(NavRoutes.TeacherDashboard.route) {
            TeacherDashboard(navController)
        }
        composable(NavRoutes.StudentDashboard.route) {
            StudentDashboard(navController)
        }
    }
}
