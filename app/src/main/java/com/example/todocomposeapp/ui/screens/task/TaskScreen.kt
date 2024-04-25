package com.example.todocomposeapp.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.todocomposeapp.data.models.Priority
import com.example.todocomposeapp.data.models.Task
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.Action


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    navigateToListScreen: (Action) -> Unit,
    selectedTask: Task?,
    sharedViewModel: SharedViewModel
) {
    // Values observed from view model
    val taskTitle: String = sharedViewModel.title
    val taskDescription: String = sharedViewModel.description
    val taskPriority: Priority = sharedViewModel.priority

    val context = LocalContext.current

    HandleBackButton(onBackPressed = {navigateToListScreen(Action.NO_ACTION)})

    Scaffold(
        topBar = {
            TaskAppBar(
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION)
                        navigateToListScreen(action)
                    // validate fields only if clicked to save task (Action.SAVE)
                    else {
                        if (sharedViewModel.validateFields())
                            navigateToListScreen(action)
                        else
                            displayToast(context)

                    }

                },
                selectedTask = selectedTask
            )
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TaskContent(
                // value got from view model
                title = taskTitle,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = taskDescription,
                onDescriptionChange = {
                    sharedViewModel.updateDescription(it)
                },
                priority = taskPriority,
                onPrioritySelected = {
                    sharedViewModel.updatePriority(it)
                }
            )
        }

    }
}

@Composable
fun HandleBackButton(
    backDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backDispatcher) {
        backDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }

}

fun displayToast(context: Context) {
    Toast.makeText(context, "Fields empty!", Toast.LENGTH_SHORT).show()
}
