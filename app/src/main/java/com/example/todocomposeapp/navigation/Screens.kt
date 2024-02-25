package com.example.todocomposeapp.navigation

import androidx.navigation.NavHostController
import com.example.todocomposeapp.util.Action
import com.example.todocomposeapp.util.Constants

class Screens(navController: NavHostController) {

    val list: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(Constants.LIST_SCREEN){ inclusive = true }
        }
    }

    val task: (Int) -> Unit = { taskId ->
        navController.navigate("task/${taskId}")
    }

}