package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.Choice.ChoiceScreen
import com.example.inventory.ui.edit.WordEditScreen
import com.example.inventory.ui.home.HomeScreen

@Composable
fun WordNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(route = "home") {
            HomeScreen(
                navigateToEdit = { wordId ->
                    if (wordId != null) {
                        navController.navigate("edit/$wordId")
                    } else {
                        navController.navigate("edit")
                    }
                },
                navigateToChoice = { navController.navigate("choice") }
            )
        }
        composable(
            route = "edit/{wordId}",
            arguments = listOf(navArgument("wordId") {
                type = NavType.IntType
            })
        ) {
            WordEditScreen(navigateBack = { navController.popBackStack() })
        }
        composable(route = "edit") {
            WordEditScreen(navigateBack = { navController.popBackStack() })
        }
        composable(route = "choice") {
            ChoiceScreen(navigateBack = { navController.popBackStack() })
        }
    }
}