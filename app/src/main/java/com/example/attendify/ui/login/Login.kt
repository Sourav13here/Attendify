package com.example.attendify.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.R
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.login.components.UserLoginInfoCard
import com.example.attendify.ui.theme.AttendifyTheme
import com.example.attendify.ui.theme.BackgroundColor
import com.example.attendify.ui.theme.CharcoalBlue
import com.example.attendify.ui.theme.PrimaryColor
import com.example.attendify.ui.theme.SecondaryColor
import com.example.attendify.ui.theme.SurfaceColor

@Composable
fun Login(navController: NavController, viewModel: LoginViewModel) {
    AppScaffold(
        title = "Login",
        navController = navController,
        titleTextStyle = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp), // reserve space for fixed bottom bar
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45f)
                        .background(SurfaceColor),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                UserLoginInfoCard(viewModel, navController)
            }

            // 🔽 Bottom fixed Sign-Up Section
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(CharcoalBlue)
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Don't have an account?", fontSize = 14.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    CustomButton(
                        text = "Sign Up",
                        action = {
                            navController.navigate(NavRoutes.SignUpPage.route) {
                                launchSingleTop = true
                            }
                        },
                        isLoadingIcon = false
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun DisplayLogin() {
    val loginViewModel: LoginViewModel = hiltViewModel()
    AttendifyTheme {
        Login(rememberNavController(), loginViewModel)
    }
}