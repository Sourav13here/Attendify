package com.example.attendify.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendify.R
import com.example.attendify.common.ext.customIconButton

//import com.google.accompanist.systemuicontroller.rememberSystemUiController

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

//    val systemUiController = rememberSystemUiController()
//    val useDarkIcons = true  // dark icons for light background
//
//    LaunchedEffect(systemUiController, useDarkIcons) {
//        systemUiController.setStatusBarColor(
//            color = BackgroundColor,
//            darkIcons = useDarkIcons
//        )
//        // Optional: Also set navigation bar color to match
//        systemUiController.setNavigationBarColor(
//            color = BackgroundColor,
//            darkIcons = useDarkIcons
//        )
//    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding().background(Color.White),
                title = { Text(title, style = MaterialTheme.typography.headlineMedium, maxLines = 1, overflow = TextOverflow.Ellipsis) },
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
                                painter = painterResource(R.drawable.ic_launcher),
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