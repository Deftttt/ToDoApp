package com.example.todocomposeapp.navigation

import androidx.navigation.NavHostController
import com.example.todocomposeapp.util.Action
import com.example.todocomposeapp.util.Constants

class Screens(navController: NavHostController) {

    val splash: () -> Unit = {
        navController.navigate("list/${Action.NO_ACTION}"){
            popUpTo(Constants.SPLASH_SCREEN){ inclusive = true }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/${taskId}")
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(Constants.LIST_SCREEN){ inclusive = true }
        }
    }


}