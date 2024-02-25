package com.example.todocomposeapp.ui.screens.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todocomposeapp.R
import com.example.todocomposeapp.components.PriorityItem
import com.example.todocomposeapp.data.models.Priority
import com.example.todocomposeapp.ui.viewmodels.SharedViewModel
import com.example.todocomposeapp.util.SearchAppBarState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    Crossfade(
        targetState = searchAppBarState,
        animationSpec = tween(250)
    )
    { targetState ->
        when (targetState) {

            SearchAppBarState.CLOSED -> {
                DefaultListAppBar(
                    onSearchClicked = {
                        sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                    },
                    onSortClicked = {},
                    onDeleteClicked = {}
                )
            }

            else -> {
                SearchAppBar(
                    text = searchTextState,
                    onTextChanged = {
                        sharedViewModel.searchTextState.value = it
                    },
                    onCloseClicked = {
                        sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                        sharedViewModel.searchTextState.value = ""
                    },
                    onSearchClicked = {}
                )
            }
        }
    }



}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = "Tasks")
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteClicked = onDeleteClicked
            )
        }
    )
}


@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit
) {
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteClicked = onDeleteClicked)
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = onSearchClicked) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search tasks")
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { isExpanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = "Sort tasks"
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {

            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.LOW) },
                onClick = {
                    isExpanded = false
                    onSortClicked(Priority.LOW)
                }
            )
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.HIGH) },
                onClick = {
                    isExpanded = false
                    onSortClicked(Priority.HIGH)
                }
            )
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.NONE) },
                onClick = {
                    isExpanded = false
                    onSortClicked(Priority.NONE)
                }
            )
        }
    }
}


@Composable
fun DeleteAllAction(
    onDeleteClicked: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { isExpanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_show_more),
            contentDescription = "Show more"
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {

            DropdownMenuItem(
                text = { Text("Delete All Tasks") },
                onClick = {
                    isExpanded = false
                    onDeleteClicked()
                }
            )
        }

    }
}


// Search App Bar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            shape = RoundedCornerShape(0.dp),
            onValueChange = {
                onTextChanged(it)
            },
            placeholder = {
                Text(text = "Search")
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        //tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if(text.isNotEmpty()){
                            onTextChanged("")
                        }
                        else{
                            onCloseClicked()
                        }

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            )


        )

    }

}


@Composable
@Preview
fun DefaultListAppBarPreview() {
    DefaultListAppBar(onSearchClicked = {},
        onSortClicked = {},
        onDeleteClicked = {}
    )
}

@Composable
@Preview
fun DefaultSearchAppBarPreview() {
    SearchAppBar(text = "", onTextChanged = {}, onCloseClicked = {}, onSearchClicked = {})
}