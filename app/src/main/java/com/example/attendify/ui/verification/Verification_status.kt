package com.example.attendify.ui.verification

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.LogoutButton
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.CardColour
import com.example.attendify.ui.theme.CharcoalBlue
import com.example.attendify.ui.theme.CorrectColor
import com.example.attendify.ui.theme.PrimaryColor
import com.example.attendify.ui.theme.PrimaryVariant
import com.example.attendify.ui.theme.SecondaryColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VerificationStatus(
    navController: NavController,
    viewmodel: VerificationViewModel,
    userType: String,
    username: String,
    branch: String,
    semester: String?,
    roll: String?
) {
    var isResendDisabled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val isLoading by viewmodel.isLoading.collectAsState()
    val context = LocalContext.current
    val navigateToStudentDashboard by viewmodel.navigateToStudentDashboard.collectAsState()
    val navigateToTeacherDashboard by viewmodel.navigateToTeacherDashboard.collectAsState()
    val userData by viewmodel.userData.collectAsState()
    val showEmailNotVerifiedBox by viewmodel.showEmailNotVerifiedBox.collectAsState()
    val snackbarMessage by viewmodel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewmodel.verifyAndSaveUser(userType, username, branch, semester, roll)
        viewmodel.refreshData()
    }

    // Dedicated polling effect — only when email not verified
    LaunchedEffect(viewmodel.showEmailNotVerifiedBox.collectAsState().value) {
        if (viewmodel.showEmailNotVerifiedBox.value) {
            viewmodel.startEmailVerificationPolling()
        } else {
            viewmodel.stopEmailVerificationPolling()
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewmodel.clearSnackbar()
        }
    }

    LaunchedEffect(userData) {
        userData?.let {
            val name = when (it) {
                is UserData.StudentData -> it.student.name
                is UserData.TeacherData -> it.teacher.name
            }
            Toast.makeText(context, "Welcome $name", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(navigateToStudentDashboard) {
        if (navigateToStudentDashboard) {
            navController.navigate(NavRoutes.StudentDashboard.route) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(navigateToTeacherDashboard) {
        if (navigateToTeacherDashboard) {
            navController.navigate(NavRoutes.TeacherDashboard.route) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
        }
    }

    AppScaffold(
        title = "Verification Status",
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        actions = {
            LogoutButton(navController, NavRoutes.VerificationStatus.route)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.fillMaxHeight(0.05f))
            // User Info Card
            userData?.let { data ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f),
                    colors = CardDefaults.cardColors(containerColor = CardColour),
                    border = BorderStroke(2.dp, CharcoalBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            "YOUR DETAILS",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(top =16.dp)
                        )
                        Spacer(modifier = Modifier.heightIn(min=8.dp, max=16.dp))

                        val details = when (data) {
                            is UserData.StudentData -> listOf(
                                "Name" to data.student.name,
                                "Email" to data.student.email,
                                "Branch" to data.student.branch,
                                "Semester" to data.student.semester,
                                "Roll No" to data.student.rollNumber
                            )

                            is UserData.TeacherData -> listOf(
                                "Name" to data.teacher.name,
                                "Email" to data.teacher.email,
                                "Branch" to data.teacher.branch
                            )
                        }


                        details.forEach { (label, value) ->
                            if (label == "Email") {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("$label: ")
                                        }
                                        withStyle(
                                            SpanStyle(
                                                color = SecondaryColor,
                                                fontWeight = FontWeight.Medium,
                                                textDecoration = TextDecoration.Underline
                                            )
                                        ) {
                                            append(value)
                                        }
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .padding(vertical = 2.dp)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_MAIN).apply {
                                                addCategory(Intent.CATEGORY_APP_EMAIL)
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            try {
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    "No email app found",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                )
                            } else {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append("$label: ")
                                        }
                                        withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                                            append(value)
                                        }
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            } ?: Text("Loading your details...", fontSize = 16.sp)

            Spacer(Modifier.heightIn(min= 24.dp, max = 32.dp))

            // Email Not Verified Box
            if (showEmailNotVerifiedBox) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_MAIN).apply {
                                addCategory(Intent.CATEGORY_APP_EMAIL)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.92f) // semi-transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    border = BorderStroke(
                        1.dp,
                        Brush.radialGradient(
                            colors = listOf(SecondaryColor, Color.White),
                            radius = 700f
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "EMAIL NOT VERIFIED!",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Check your inbox for verification link",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Resend verification email",
                                tint = if (isResendDisabled) Color.Gray else SecondaryColor,
                                modifier = Modifier
                                    .size(30.dp)
                                    .alpha(if (isResendDisabled) 0.4f else 1f)
                                    .clickable(
                                        enabled = !isResendDisabled,
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                                        Toast.makeText(
                                            context,
                                            "Verification email resent",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        isResendDisabled = true

                                        // Re-enable after delay
                                        coroutineScope.launch {
                                            delay(5000L) // 5 seconds
                                            isResendDisabled = false
                                        }
                                    }
                            )
                            Text(
                                text = "Resend",
                                color = if (isResendDisabled) Color.Gray else SecondaryColor,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE6FFEA)), // soft green
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    border = BorderStroke(
                        2.dp, brush = Brush.radialGradient(
                            colors = listOf(CorrectColor, Color.White),
                            radius = 700f
                        )
                    ) // green border
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),

                    ) {
                        Row(Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                "EMAIL VERIFIED",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = CorrectColor // dark green
                            )
                            Icon(
                                imageVector = Icons.Filled.Check,
                                tint = CorrectColor,
                                contentDescription = "Verification Done")

                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "One step closer to your dashboard",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Left
                        )
                    }
                }
            }

            Spacer(Modifier.fillMaxHeight(0.18f))

            // Not Verified Box
            Card(
                modifier = Modifier.fillMaxWidth(0.85f).padding(start = 16.dp,end = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                border = BorderStroke(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(PrimaryVariant, SecondaryColor.copy(0.6f))
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Your account is not verified yet.",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Please wait for the admin to verify your account.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {
                            Log.e("verification", "clicked")
                            viewmodel.refreshData()
                            viewmodel.showSnackbar("Refreshing your status")
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoading) Color.Gray else PrimaryColor
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Refresh", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
