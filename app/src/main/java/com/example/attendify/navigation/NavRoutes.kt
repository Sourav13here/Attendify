package com.example.attendify.navigation

sealed class NavRoutes(val route: String) {
    object LoginPage : NavRoutes("login")
    object SignUpPage: NavRoutes("sign up")
}