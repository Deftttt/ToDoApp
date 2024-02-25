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
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todocomposeapp.R
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.Action
import com.example.todocomposeapp.util.SearchAppBarState
import com.google.android.material.search.SearchBar
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
   // Executed only once
    LaunchedEffect(key1 = true){
        sharedViewModel.getAllTasks()
    }

    // Observe data from database
    val allTasks by sharedViewModel.allTasks.collectAsState()

    // Gets value from SharedViewModel
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    // Observe action
    val action by sharedViewModel.action

    val snackbarHostState = remember {SnackbarHostState()}

    // When ListScreen triggers check action and handle database actions
    DisplaySnackBar(
        snackbarHostState = snackbarHostState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action) },
        taskTitle = sharedViewModel.title.value,
        action = action
    )


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState ) },
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)){
                ListContent(tasks = allTasks, navigateToTaskScreen = navigateToTaskScreen)
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
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    action: Action
){
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        if(action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = "${action.name}: $taskTitle",
                    actionLabel = "OK"
                )
            }
        }
    }

}

