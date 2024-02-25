package com.example.todocomposeapp.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocomposeapp.data.models.Priority
import com.example.todocomposeapp.data.models.Task
import com.example.todocomposeapp.data.repositories.ToDoRepository
import com.example.todocomposeapp.util.Action
import com.example.todocomposeapp.util.Constants
import com.example.todocomposeapp.util.RequestState
import com.example.todocomposeapp.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    // Values when editing/adding task
    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    // Search app bar
    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")


    // All tasks
    private val _allTasks = MutableStateFlow<RequestState<List<Task>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<Task>>> = _allTasks

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading // While loading from database
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it) // When collected all data
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e) // If error occurs
        }
    }

    // Selected task
    private val _selectedTask: MutableStateFlow<Task?> = MutableStateFlow(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    fun getTaskById(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }


    fun updateTaskFields(selectedTask: Task?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }


    fun updateTitle(newTitle: String) {
        if (newTitle.length <= Constants.MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }


    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }


    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.addTask(task)
        }
    }

    fun handleDatabaseActions(action: Action){
        when(action){
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
            }
            Action.DELETE -> {
            }
            Action.DELETE_ALL -> {
            }
            Action.UNDO -> {
            }
            else -> {

            }
        }

        // change SharedViewModel action to default value (NO_ACTION)
        this.action.value = Action.NO_ACTION

    }



}