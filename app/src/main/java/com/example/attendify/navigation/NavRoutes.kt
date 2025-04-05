package com.example.attendify.navigation

sealed class NavRoutes(val route: String) {
    object LoginPage : NavRoutes("login")
    object SignUpPage: NavRoutes("sign up")
    object VerificationStatus: NavRoutes("verification status")
    object TeacherDashboard: NavRoutes("teacher dashboard")
    object StudentDashboard: NavRoutes("student dashboard")

}