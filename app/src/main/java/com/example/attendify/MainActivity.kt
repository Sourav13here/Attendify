package com.example.attendify

import AttendifyAppUi
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val view = LocalView.current

            SideEffect {
                // Control system bars appearance
                val windowInsetsController = WindowInsetsControllerCompat(window, view)

                // Set your desired status bar color (example: white)
                window.statusBarColor = androidx.compose.ui.graphics.Color.White.toArgb()

                // Set true if you want dark icons (for light background)
                windowInsetsController.isAppearanceLightStatusBars = true
            }

            AttendifyAppUi()
        }
    }
}
