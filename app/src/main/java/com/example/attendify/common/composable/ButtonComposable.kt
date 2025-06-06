package com.example.attendify.common.composable

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.attendify.R
import com.example.attendify.common.ext.customButton
import com.example.attendify.common.viewmodel.CommonViewmodel
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.PrimaryColor
import com.example.attendify.ui.theme.PrimaryVariant
import com.example.attendify.ui.theme.TextOnPrimary
import com.example.attendify.ui.verification.components.LogoutConfirmationDialog
import kotlinx.coroutines.launch

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    action: () -> Unit,
    isLoadingIcon: Boolean = false
) {
    Button(
        onClick = action,
        modifier = modifier.customButton(),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryColor,
            contentColor = TextOnPrimary
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text,fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(4.dp))
            if (isLoadingIcon) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp
                )
            }
        }
    }
}

@Composable
fun CustomTextButton(text: String, modifier: Modifier = Modifier, action: () -> Unit) {
    TextButton(
        onClick = action
    ) {
        Text(text = text, color = Color.Black)
    }
}


@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    contentDescription: String? = null,
    size: Dp = 28.dp,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        when {
            imageVector != null -> {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription.toString(),
                    modifier = modifier.size(size)
                )
            }

            painter != null -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription.toString(),
                    modifier = modifier.size(size)
                )
            }
        }
    }
}

@Composable
fun LogoutButton(
    navController: NavController,
    popUpToRoute : String
) {
    var showLogOutDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val viewModel: CommonViewmodel = hiltViewModel()

    if (showLogOutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                coroutineScope.launch {
                    viewModel.signOut()
                    navController.navigate(NavRoutes.LoginPage.route) {
                        popUpTo(popUpToRoute) { inclusive = true }
                    }
                }
                showLogOutDialog = false
            },
            onDismiss = { showLogOutDialog = false }
        )
    }

    IconButton(
        onClick = { showLogOutDialog = true },
        modifier = Modifier
            .clip(CircleShape).size(32.dp)
            .background(PrimaryColor)
            .border(2.dp, TextOnPrimary, CircleShape)

    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = "Logout button",
            tint = Color.White
        )
    }
}



//@Preview(showBackground = true)
//@Composable
//fun PreviewLogoutButton() {
//    LogoutButton()
//}