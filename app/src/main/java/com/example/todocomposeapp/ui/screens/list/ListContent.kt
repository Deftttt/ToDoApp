package com.example.todocomposeapp.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todocomposeapp.data.models.Priority
import com.example.todocomposeapp.data.models.Task
import com.example.todocomposeapp.ui.theme.HighPriorityColor
import com.example.todocomposeapp.util.Action
import com.example.todocomposeapp.util.RequestState
import com.example.todocomposeapp.util.SearchAppBarState


@Composable
fun ListContent(
    tasks: RequestState<List<Task>>,
    searchedTasks: RequestState<List<Task>>,
    lowPriorityTasks: List<Task>,
    highPriorityTasks: List<Task>,
    sortState: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    onSwipeToDelete: (Task, Action) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                // Only when loading data finished
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }

            sortState.data == Priority.NONE -> {
                if (tasks is RequestState.Success) {
                    HandleListContent(
                        tasks = tasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }

            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }

            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }

        }


    }
}


@Composable
fun HandleListContent(
    tasks: List<Task>,
    onSwipeToDelete: (Task, Action) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        DisplayTasksList(
            tasks = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTasksList(
    tasks: List<Task>,
    onSwipeToDelete: (Task, Action) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn {
        items(
            items = tasks,
            key = { task ->
                task.id
            }
        ) {
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                onSwipeToDelete(it, Action.DELETE)
            }

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = { DeleteBackground() },
                dismissContent = {
                    TaskItem(task = it, navigateToTaskScreen = navigateToTaskScreen)
                }
            )
        }

    }
}


@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete icon",
            tint = Color.White
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RectangleShape,
        onClick = {
            navigateToTaskScreen(task.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(8f),
                    text = task.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(16.dp)
                    ) {
                        drawCircle(color = task.priority.color)
                    }
                }
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = task.description,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis

            )
        }
    }
}


@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(
        task = Task(
            title = "Task 1",
            description = "Do dishes and bla bla",
            priority = Priority.MEDIUM
        ), navigateToTaskScreen = {}
    )
}
