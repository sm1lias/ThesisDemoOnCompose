package com.example.newdemo

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.newdemo.mainscreen.MainScreen
import com.example.newdemo.resultscreen.ResultScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.ResultScreen.route + "/{image_path}/{forScreen}",
            arguments = listOf(
                navArgument("image_path") {
                    type = NavType.StringType
                },
                navArgument("forScreen") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            ResultScreen(
                imagePath = entry.arguments?.getString("image_path"),
                forScreen = entry.arguments?.getString("forScreen")
            )

        }
    }

}