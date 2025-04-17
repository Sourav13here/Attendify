package com.example.attendify.navigation

sealed class NavRoutes(val route: String) {
    object LoginPage : NavRoutes("login")
    object SignUpPage: NavRoutes("sign up")
    object VerificationStatus: NavRoutes("verification_status/{username}/{branch}/{semester}/{roll}"){
        fun createRoute(username:String, branch:String,semester:String,roll:String ): String{
            return   "verification_status/$username/$branch/$semester/$roll"

        }
    }
    object TeacherDashboard: NavRoutes("teacher dashboard")
    object StudentDashboard: NavRoutes("student dashboard")
    object SplashScreen: NavRoutes("splash screen")

}