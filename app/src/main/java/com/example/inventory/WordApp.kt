package com.example.inventory

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.inventory.ui.navigation.WordNavHost

@Composable
fun WordApp() {
    val navController = rememberNavController()
    WordNavHost(navController = navController)
}