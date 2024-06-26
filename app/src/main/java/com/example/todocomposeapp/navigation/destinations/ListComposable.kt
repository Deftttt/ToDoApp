package com.example.todocomposeapp.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todocomposeapp.ui.screens.list.ListScreen
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.Constants
import com.example.todocomposeapp.util.toAction

// Extension function
fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
){
    composable(
        route = Constants.LIST_SCREEN,
        arguments = listOf(navArgument(Constants.LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
        // Gets Action from nav argument
        val action = it.arguments?.getString(Constants.LIST_ARGUMENT_KEY).toAction()

        // Change action value in SharedViewModel
        LaunchedEffect(key1 = action){
            sharedViewModel.updateAction(action)
        }

        val dbAction = sharedViewModel.action

        ListScreen(navigateToTaskScreen = navigateToTaskScreen, sharedViewModel = sharedViewModel, action = dbAction)

    }
}