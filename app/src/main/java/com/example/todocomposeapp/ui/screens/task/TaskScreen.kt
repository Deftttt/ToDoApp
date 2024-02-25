package com.example.todocomposeapp.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todocomposeapp.data.models.Priority
import com.example.todocomposeapp.data.models.Task
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.Action


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navigateToListScreen: (Action) -> Unit,
    selectedTask: Task?,
    sharedViewModel: SharedViewModel
) {
    // Values we're observing from view model
    val taskTitle: String by sharedViewModel.title
    val taskDescription: String by sharedViewModel.description
    val taskPriority: Priority by sharedViewModel.priority

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TaskAppBar(
                navigateToListScreen = { action ->
                    if(action == Action.NO_ACTION)
                        navigateToListScreen(action)
                    // validate fields only if clicked to save task (Action.SAVE)
                    else {
                        if(sharedViewModel.validateFields())
                            navigateToListScreen(action)
                        else
                            displayToast(context)

                    }

                },
                selectedTask = selectedTask
            )
        }
    ) // Inner padding so topBar is not overlapping content
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TaskContent(
                // value got from view model
                title = taskTitle,
                onTitleChange = {
                    // change value in view model
                    sharedViewModel.updateTitle(it)
                },
                description = taskDescription,
                onDescriptionChange = {
                    sharedViewModel.description.value = it
                },
                priority = taskPriority,
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                }
            )
        }

    }
}


fun displayToast(context: Context){
    Toast.makeText(context, "Fields empty!", Toast.LENGTH_SHORT).show()
}
