package com.example.todocomposeapp.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.todocomposeapp.R
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.Action
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    // Executed only once
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    }

    // When action changes, handle database actions
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action)
    }

    // Observe data
    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPrioTasks by sharedViewModel.lowPrioTasks.collectAsState()
    val highPrioTasks by sharedViewModel.highPrioTasks.collectAsState()

    // Gets value from SharedViewModel
    val searchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState = sharedViewModel.searchTextState

    val snackbarHostState = remember { SnackbarHostState() }

    // When ListScreen triggers check action and handle database actions
    DisplaySnackBar(
        snackbarHostState = snackbarHostState,
        onComplete = { sharedViewModel.updateAction(it) },
        onUndoClicked = { sharedViewModel.updateAction(it) },
        taskTitle = sharedViewModel.title,
        action = action
    )


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                ListContent(
                    tasks = allTasks,
                    searchedTasks = searchedTasks,
                    searchAppBarState = searchAppBarState,
                    navigateToTaskScreen = navigateToTaskScreen,
                    lowPriorityTasks = lowPrioTasks,
                    highPriorityTasks = highPrioTasks,
                    onSwipeToDelete = { task, action ->
                        sharedViewModel.updateAction(action)
                        sharedViewModel.updateTaskFields(task)
                        snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    sortState = sortState
                )
            }

        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }

    )
}


@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button)
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackbarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = setMessage(action, taskTitle),
                    actionLabel = setActionLabel(action)
                )
                undoDeleteTaskAction(action, snackBarResult, onUndoClicked)

            }
            onComplete(Action.NO_ACTION)
        }
    }

}

private fun setMessage(action: Action, taskTitle: String): String {
    return when (action) {
        Action.DELETE -> "Deleted: $taskTitle"
        Action.UNDO -> "Restored: $taskTitle"
        Action.DELETE_ALL -> "All tasks deleted"
        Action.ADD -> "Added new task: $taskTitle"
        else -> "${action.name} $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return when (action) {
        Action.DELETE -> "Undo"
        else -> "OK"
    }
}

private fun undoDeleteTaskAction(
    action: Action,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackbarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }

}

