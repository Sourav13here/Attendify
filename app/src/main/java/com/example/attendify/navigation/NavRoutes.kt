package com.example.attendify.navigation

sealed class NavRoutes(val route: String) {
    object LoginPage : NavRoutes("login")
    object SignUpPage : NavRoutes("sign up")
    object VerificationStatus : NavRoutes("verification_status/{userType}/{username}/{branch}?semester={semester}&roll={roll}") {
        fun createRoute(userType: String, username: String, branch: String, semester: String? = null, roll: String? = null): String {
            return buildString {
                append("verification_status/$userType/$username/$branch")
                if (!semester.isNullOrEmpty()) append("?semester=$semester")
                if (!roll.isNullOrEmpty()) append("&roll=$roll")
            }
        }
    }
    object TeacherDashboard : NavRoutes("teacher dashboard")
    object StudentDashboard : NavRoutes("student dashboard")
    object SplashScreen : NavRoutes("splash screen")
    object VerificationPage: NavRoutes("verification page")
    object AttendanceStudent : NavRoutes("attendance_student/{subjectName}/{subjectCode}") {
        fun createRoute(subjectName: String, subjectCode: String): String {
            return "attendance_student/$subjectName/$subjectCode"
        }
    }

    object AttendanceSheet : NavRoutes("Attendance Sheet")
}