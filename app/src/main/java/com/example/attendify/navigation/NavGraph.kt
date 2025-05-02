package com.example.attendify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendify.ui.login.Login
import com.example.attendify.ui.login.LoginViewModel
import com.example.attendify.ui.sign_up.SignUp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.attendify.ui.sign_up.SignUpViewModel
import com.example.attendify.ui.splashscreen.SplashScreen
import com.example.attendify.ui.splashscreen.SplashViewModel
import com.example.attendify.ui.student.AttendanceStudent
import com.example.attendify.ui.student.StudentDashboard
import com.example.attendify.ui.student.StudentDashboardViewModel
import com.example.attendify.ui.teacher.AttendanceSheet
import com.example.attendify.ui.teacher.TeacherDashboard
import com.example.attendify.ui.teacher.TeacherDashboardViewModel
import com.example.attendify.ui.verification.VerificationStatus
import com.example.attendify.ui.verification.VerificationViewModel
import com.example.attendify.ui.verification.Verification_Page

@Composable
fun NavGraph(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val verificationViewModel: VerificationViewModel = hiltViewModel()
    val splashViewModel: SplashViewModel = hiltViewModel()
    val studentDashboardViewModel: StudentDashboardViewModel = hiltViewModel()
    val teacherDashboardViewModel: TeacherDashboardViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SplashScreen.route
    ) {
        composable(NavRoutes.LoginPage.route) {
            Login(navController, loginViewModel)
        }
        composable(NavRoutes.SignUpPage.route) {
            SignUp(navController, signUpViewModel)
        }
        composable(
            route = NavRoutes.VerificationStatus.route,
            arguments = listOf(
                navArgument("username"){
                    type = NavType.StringType
                },
                navArgument("branch"){
                    type = NavType.StringType
                },
                navArgument("semester"){
                    type = NavType.StringType
                    defaultValue = "null"
                    nullable = true
                },
                navArgument("roll"){
                    type = NavType.StringType
                    defaultValue = "null"
                    nullable = true
                }
            )
        ) {
            backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "student"
            val username = backStackEntry.arguments?.getString("username") ?: "N/A"
            val branch = backStackEntry.arguments?.getString("branch") ?: "N/A"
            val semester = backStackEntry.arguments?.getString("semester")
            val roll = backStackEntry.arguments?.getString("roll")

            VerificationStatus(
                navController = navController,
                viewmodel = verificationViewModel,
                userType = userType,
                username = username,
                branch = branch,
                semester = semester,
                roll = roll
            )
        }
        composable(NavRoutes.TeacherDashboard.route) {
            TeacherDashboard(navController, teacherDashboardViewModel)
        }
        composable(NavRoutes.StudentDashboard.route) {
            StudentDashboard(navController, studentDashboardViewModel)
        }
        composable(NavRoutes.SplashScreen.route) {
            SplashScreen(navController, splashViewModel)
        }
        composable(NavRoutes.VerificationPage.route) {
            Verification_Page(navController,verificationViewModel)
        }

        composable(
            route = "attendance_student/{subjectName}/{subjectCode}/{branch}/{semester}/{studentEmail}",
            arguments = listOf(
                navArgument("subjectName") { type = NavType.StringType },
                navArgument("subjectCode") { type = NavType.StringType },
                navArgument("branch") { type = NavType.StringType },
                navArgument("semester") { type = NavType.StringType },
                navArgument("studentEmail") { type = NavType.StringType }
            )

        ) { backStackEntry ->
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
            val subjectCode = backStackEntry.arguments?.getString("subjectCode") ?: ""
            val branch = backStackEntry.arguments?.getString("branch") ?: ""
            val semester = backStackEntry.arguments?.getString("semester") ?: ""
            val studentEmail = backStackEntry.arguments?.getString("studentEmail") ?: ""

            AttendanceStudent(
                navController = navController,
                subjectName = subjectName,
                subjectCode = subjectCode,
                branch = branch,
                semester = semester,
                studentEmail = studentEmail
            )

        }




        composable(
            route = "${NavRoutes.AttendanceSheet.route}/{subjectCode}/{subjectName}/{branch}/{semester}/{teacherEmail}"
        ) { backStackEntry ->
            val subjectCode = backStackEntry.arguments?.getString("subjectCode") ?: ""
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
            val branch = backStackEntry.arguments?.getString("branch") ?: ""
            val semester = backStackEntry.arguments?.getString("semester") ?: ""
            val teacherEmail = backStackEntry.arguments?.getString("teacherEmail") ?: ""
            AttendanceSheet(
                navController = navController,
                subjectCode = subjectCode,
                subjectName = subjectName,
                branch = branch,
                semester = semester,
                teacherEmail = teacherEmail
            )
        }


    }
}
