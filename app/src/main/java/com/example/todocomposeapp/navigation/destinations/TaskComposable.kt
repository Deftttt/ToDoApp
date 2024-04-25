package com.example.todocomposeapp.navigation.destinations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todocomposeapp.ui.screens.task.TaskScreen
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.Action
import com.example.todocomposeapp.util.Constants

// Extension function
fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: SharedViewModel
){
    composable(
        route = Constants.TASK_SCREEN,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 400
                ));
        },
        arguments = listOf(navArgument(Constants.TASK_ARGUMENT_KEY){
            type = NavType.IntType
        })
    ){
        // Gets id from nav argument and gets task of that id
        val taskId = it.arguments!!.getInt(Constants.TASK_ARGUMENT_KEY)
        LaunchedEffect(key1 = taskId){
            sharedViewModel.getTaskById(taskId)
        }
        // observe value, if it changes recompose TaskScreen
        val selectedTask by sharedViewModel.selectedTask.collectAsState()

        // When selectedTask changes, update task fields
        LaunchedEffect(key1 = selectedTask){
            if(selectedTask != null || taskId == -1){
                sharedViewModel.updateTaskFields(selectedTask)
            }

        }

        TaskScreen(navigateToListScreen, selectedTask, sharedViewModel)
    }
}