package com.example.attendify


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.attendify.navigation.NavGraph
import com.example.attendify.ui.theme.AttendifyTheme


@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendifyApp(){
    AttendifyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavGraph(rememberNavController())
        }
    }
}