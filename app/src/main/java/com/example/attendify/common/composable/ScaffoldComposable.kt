package com.example.attendify.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendify.common.ext.customIconButton
import com.example.attendify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    navController: NavController? = null,
    showBackButton: Boolean = false,
    contentDescriptionBackButton: String? = null,
    showLogo: Boolean = false,
    snackbarHost: @Composable () -> Unit = {},
    contentDescriptionLogo: String? = null,
    showTeacherBottomNav: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable (() -> Unit)? = null,
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(

                title = { Text(title, style = titleTextStyle, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    when {
                        showBackButton -> {
                            CustomIconButton(
                                modifier = Modifier.customIconButton(),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = contentDescriptionBackButton.toString(),
                                onClick = { navController?.popBackStack() }
                            )
                        }
                        showLogo -> {
                            CustomIconButton(
                                modifier = Modifier.customIconButton(),
                                painter = painterResource(R.drawable.app_logo),
                                contentDescription = contentDescriptionLogo.toString(),
                                onClick = {},
                                size = 36.dp
                            )
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White, // 👈 background behind the title
                    titleContentColor = Color.Black // optional: title text color
                )
            )
        },
        content = content
    )
}