package com.example.attendify.ui.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.attendify.R
import com.example.attendify.common.composable.AppScaffold
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomOutlinedTextField
import com.example.attendify.common.composable.CustomTextButton
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.AttendifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordDialog(
    onDismiss: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    BasicAlertDialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Reset password", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(text = "Password reset link will be sent to your email.")
                Spacer(Modifier.height(8.dp))
                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    modifier = Modifier
                )
                Spacer(Modifier.height(12.dp))
                Row {
                    CustomTextButton(
                        text = "Cancel",
                        modifier = Modifier,
                        action = { onDismiss() }
                    )
                    CustomButton(
                        text = "Send link!",
                        modifier = Modifier,
                        action = {
                            if (email.isNotBlank()) {

                            } else {

                            }
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DisplayLogin() {
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    AttendifyTheme {
        ForgetPasswordDialog(
            onDismiss = {showForgotPasswordDialog = false}
        )
    }
}