package com.example.attendify.navigation


interface Destinations {
    val route: String
}

object SignUpPage: Destinations {
    override val route = "signup"
}

object LoginPage: Destinations {
    override val route = "login"
}

object VerificationStatus: Destinations {
    override val route = "VerificationStatus"
}

object VerificationPage: Destinations {
    override val route = "VerifyPage"
}

object StudentDashboard: Destinations {
    override val route = "StudentDashboard"
}

object TeacherDashboard: Destinations {
    override val route = "TeacherDashboard"
}
object AddSubjects: Destinations{
    override val route = "Add subject"
}