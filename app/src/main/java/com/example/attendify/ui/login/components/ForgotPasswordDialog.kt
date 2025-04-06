package com.example.attendify.ui.login.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.common.composable.CustomButton
import com.example.attendify.common.composable.CustomOutlinedTextField
import com.example.attendify.common.composable.CustomTextButton
import com.example.attendify.ui.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordDialog(
    onDismiss: () -> Unit,
    viewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }
    val resetMessage by viewModel.passwordResetMessage.collectAsState()
    val resetMessage2 by viewModel.passwordResetMessage2.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(resetMessage) {
        resetMessage?.let {
            onDismiss()
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.clearPasswordResetMessage()
    }

    BasicAlertDialog(
        onDismissRequest = {
            viewModel.clearPasswordResetMessage()
            onDismiss()
        }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Reset password", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text("Password reset link will be sent to your email.")
                Spacer(Modifier.height(8.dp))
                CustomOutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        viewModel.clearPasswordResetMessage()
                    },
                    label = "Email",
                    modifier = Modifier
                )
                Spacer(Modifier.height(12.dp))
                if (resetMessage2 != null) {
                    Text(resetMessage2!!, color = Color.Red, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Row {
                    CustomTextButton(
                        text = "Cancel",
                        modifier = Modifier,
                        action = {
                            viewModel.clearPasswordResetMessage()
                            onDismiss()
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    CustomButton(
                        text = "Send link!",
                        modifier = Modifier,
                        action = {
                            if (email.isNotBlank()) {
                                viewModel.sendPasswordResetEmail(email)
                            }
                        }
                    )
                }
            }
        }
    }
}
