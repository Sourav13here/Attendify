package com.example.attendify.ui.student


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.LogoutButton
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.verification.components.LogoutConfirmationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import com.example.attendify.ui.theme.CardColour
import com.example.attendify.ui.theme.CharcoalBlue
import com.example.attendify.ui.theme.CorrectColor
import com.example.attendify.ui.theme.DarkRed
import com.example.attendify.ui.theme.DarkYellow
import com.example.attendify.ui.theme.ErrorColor
import com.example.attendify.ui.theme.SecondaryColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun StudentDashboard(navController: NavController, viewModel: StudentDashboardViewModel) {

    var showLogOutDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)



    LaunchedEffect(Unit) {
        viewModel.fetchStudentData()
    }


//    LaunchedEffect(Unit) {
//        val userId = viewModel.authRepo.getCurrentUser()?.uid
//        if (userId == null) {
//            navController.navigate(NavRoutes.LoginPage.route) {
//                popUpTo(NavRoutes.StudentDashboard.route) { inclusive = true }
//            }
//        }
//    }

    if (showLogOutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.signOut()
                    navController.navigate(NavRoutes.LoginPage.route) {
                        popUpTo(NavRoutes.StudentDashboard.route) { inclusive = true }
                    }
                }
                showLogOutDialog = false
            },
            onDismiss = {
                showLogOutDialog = false
            }
        )
    }

    AppScaffold(
        title = "Student Dashboard",
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        actions = {
            LogoutButton(
                navController = navController,
                popUpToRoute = NavRoutes.StudentDashboard.route
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .fillMaxWidth(0.9f)
                .padding(start = 20.dp,end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            val student = viewModel.student.value
            val subjectsWithAttendance = viewModel.subjectAttendancePairs.value

            if (student != null) {
                Text(
                    text = buildAnnotatedString {
                        append("Welcome, ")
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(student.name)
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(2.dp, CharcoalBlue, RectangleShape)
                        .padding(12.dp),
                )
            } else {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(16.dp))


            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.fetchStudentData()
                }
            )

            {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .border(1.dp, SecondaryColor, RoundedCornerShape(16.dp)) // Border first
                        .background(SecondaryColor.copy(0.4f), RoundedCornerShape(16.dp))    // Then background
                        .padding(top =20.dp,bottom = 20.dp,start = 5.dp,end = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (subjectsWithAttendance.isEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    } else {
                        items(subjectsWithAttendance) { (subject, percentage) ->
                            AttendanceCard(
                                subject = subject.subjectName,
                                title = subject.subjectCode,
                                percentage = percentage,
                                onClick = {
                                    student?.let {
                                        navController.navigate(
                                            "attendance_student/${subject.subjectName}/${subject.subjectCode}/${it.branch}/${it.semester}/${it.email}"
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AttendanceCard(subject: String, title: String, percentage: Int, onClick: () -> Unit) {
    val color = when {
        percentage >= 75 -> CorrectColor.copy(0.8f) // Material Green
        percentage in 40..74 -> DarkYellow.copy(0.8f)// Material Amber
        percentage in 0..39 -> ErrorColor.copy(0.8f) // Material Red
        else -> Color.Gray

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = subject, fontWeight = FontWeight.Bold)
            Text(text = title, fontSize = 14.sp)
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, shape = RoundedCornerShape(20.dp)),

            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$percentage%",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}