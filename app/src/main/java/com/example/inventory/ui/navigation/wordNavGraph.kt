package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.Choice.ChoiceDestination
import com.example.inventory.ui.Choice.ChoiceScreen
import com.example.inventory.ui.edit.WordEditDestination
import com.example.inventory.ui.edit.WordEditScreen
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen

@Composable
fun WordNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToEdit = { wordId ->
                    if (wordId != null) {
                        navController.navigate("${WordEditDestination.route}/$wordId")
                    } else {
                        navController.navigate(WordEditDestination.routeWithoutArgs)
                    }
                },
                navigateToChoice = { navController.navigate(ChoiceDestination.route) }
            )
        }
        composable(
            route = WordEditDestination.routeWithArgs,
            arguments = listOf(navArgument(WordEditDestination.wordIdArg) {
                type = NavType.IntType
            })
        ) {
            WordEditScreen(navigateBack = { navController.popBackStack() })
        }
        composable(
            route = WordEditDestination.routeWithoutArgs
        ) {
            WordEditScreen(navigateBack = { navController.popBackStack() })
        }
        composable(route = ChoiceDestination.route) {
            ChoiceScreen(navigateBack = { navController.popBackStack() })
        }
    }
}