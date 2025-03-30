package com.example.attendify.navigation

sealed class NavRoutes(val route: String) {
    object HomeGuest : NavRoutes("home")
    object LoginPage : NavRoutes("login")
    object HomeStudent : NavRoutes("homepage student")
    object SignUpPage : NavRoutes("signup")
    object HomeTeacher : NavRoutes("home page teacher")
    object LoadingScreen : NavRoutes("loading screen")
    object Verification : NavRoutes("verification page")
    object SplashScreen: NavRoutes("splash screen")
    object TeacherSubjects: NavRoutes("teacher subjects")
    object TeacherProfile: NavRoutes("teacher profile")
}


