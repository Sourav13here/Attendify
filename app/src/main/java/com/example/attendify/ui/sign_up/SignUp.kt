package com.example.attendify.ui.sign_up

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomExposedDropdown
import com.example.attendify.common.composable.CustomOutlinedTextField
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.BackgroundColor
import com.example.attendify.ui.theme.GrayLight
import com.example.attendify.ui.theme.PrimaryVariant
import com.example.attendify.utils.Constants

@Composable
fun SignUp(navController: NavController, viewModel: SignUpViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("student") }
    var branch by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var rollno by remember { mutableStateOf("") }
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    val tabLabels = listOf("Students", "Teachers")
    val tabCount = tabLabels.size
    val tabWidth = 148.dp

    val indicatorOffset by animateDpAsState(
        targetValue = tabWidth * selectedTab,
        animationSpec = tween(durationMillis = 300),
        label = "Indicator Offset"
    )

    val navigateToVerification by viewModel.navigateToVerification.collectAsState()

    LaunchedEffect(navigateToVerification) {
        if (navigateToVerification) {
            navController.navigate(
                NavRoutes.VerificationStatus.createRoute(
                    userType = accountType.lowercase(),
                    username = username,
                    branch = branch,
                    semester = semester,
                    roll = rollno,
                )
            ) {
                popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
            }
            viewModel.resetNavigationState()
        }
    }

    AppScaffold(
        title = "SIGN UP",
        navController = navController,
        showBackButton = true,
        contentDescriptionBackButton = "Back",
        titleTextStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 20.dp)
        ) {
            val maxWidth = maxWidth

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 420.dp)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top content
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Enter details",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundColor, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CustomOutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Full Name",
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Outlined.Person2
                        )
                        CustomOutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Outlined.Email
                        )
                        CustomOutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            isPasswordField = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Outlined.Password
                        )

                        Spacer(Modifier.heightIn(min = 46.dp, max = 160.dp))

                        Box(
                            modifier = Modifier
                                .width(tabWidth * tabCount)
                                .padding(top= 8.dp,bottom = 16.dp)
                                .height(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(PrimaryVariant)
                                .border(2.dp,Color.Black, RoundedCornerShape(24.dp))
                                .align(Alignment.CenterHorizontally)
                        ) {
                            // Upper Slider Layer
                            Box(
                                modifier = Modifier
                                    .offset { IntOffset(indicatorOffset.roundToPx(), 0) }
                                    .width(tabWidth)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.White)
                            )

                            // Options row
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                tabLabels.forEachIndexed { index, label ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .clickable {
                                                selectedTab = index
                                                accountType = if (index == 0) "student" else "teacher"
                                                // Reset form when account type changes
                                                branch = ""
                                                semester = ""
                                                rollno = ""
                                                       },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            color = if (selectedTab == index) Color.Black else Color.White.copy(0.5f),
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    // Divider between tabs, except after last tab
                                    if (index != tabLabels.lastIndex)
                                        Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(24.dp)
                                            .background(Color.Black.copy(alpha = 0.3f))
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomExposedDropdown(
                                label = "Branch",
                                options = Constants.BRANCHES,
                                selectedOption = branch,
                                onOptionSelected = { branch = it },
                                modifier = Modifier.weight(1f)
                            )
                            if (accountType == "student") {
                                CustomExposedDropdown(
                                    label = "Semester",
                                    options = Constants.SEMESTERS,
                                    selectedOption = semester,
                                    onOptionSelected = { semester = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        if (accountType == "student") {
                            CustomOutlinedTextField(
                                value = rollno,
                                onValueChange = { rollno = it },
                                label = "Roll No.",
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = Icons.Outlined.Numbers
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CustomButton(
                                text = "Register",
                                action = {
                                    viewModel.createAccount(
                                        context,
                                        username,
                                        email,
                                        password,
                                        accountType,
                                        branch,
                                        semester,
                                        rollno
                                    )
                                }
                            )
                        }
                    }
                }

                // Bottom static button
                TextButton(
                    onClick = {
                        navController.navigate(NavRoutes.LoginPage.route) {
                            popUpTo(NavRoutes.LoginPage.route) { inclusive = true }
                        }
                    }
                ) {
                    Text("Already have an account? Login", fontSize = 13.sp)
                }
            }
        }
    }
}