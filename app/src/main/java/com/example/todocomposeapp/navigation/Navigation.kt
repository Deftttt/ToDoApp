package com.example.todocomposeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.todocomposeapp.navigation.destinations.listComposable
import com.example.todocomposeapp.navigation.destinations.splashComposable
import com.example.todocomposeapp.navigation.destinations.taskComposable
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.Constants


@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    // Save backstack of composable screens
    val screen = remember(navController){
        Screens(navController = navController)
    }

    NavHost(navController = navController, startDestination = Constants.SPLASH_SCREEN){
        // Composables (screens)
        splashComposable(navigateToListScreen = screen.splash)
        listComposable(navigateToTaskScreen = screen.list, sharedViewModel = sharedViewModel)
        taskComposable(navigateToListScreen = screen.task, sharedViewModel = sharedViewModel)

    }

}