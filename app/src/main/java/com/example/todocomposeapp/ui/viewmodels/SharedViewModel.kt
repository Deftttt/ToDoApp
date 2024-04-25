package com.example.todocomposeapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocomposeapp.data.models.Priority
import com.example.todocomposeapp.data.models.Task
import com.example.todocomposeapp.data.repositories.DataStoreRepository
import com.example.todocomposeapp.data.repositories.ToDoRepository
import com.example.todocomposeapp.util.Action
import com.example.todocomposeapp.util.Constants
import com.example.todocomposeapp.util.RequestState
import com.example.todocomposeapp.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    // Values when editing/adding task
    var id by mutableIntStateOf(0)
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(Priority.LOW)
        private set

    // Search app bar
    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set
    var searchTextState by mutableStateOf("")
        private set


    // Tasks
    private val _allTasks = MutableStateFlow<RequestState<List<Task>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<Task>>> = _allTasks

    private val _searchedTasks = MutableStateFlow<RequestState<List<Task>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<Task>>> = _searchedTasks

    val lowPrioTasks: StateFlow<List<Task>> = repository.sortedByLowPriority.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    val highPrioTasks: StateFlow<List<Task>> = repository.sortedByHighPriority.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState
    fun saveSortState(priority: Priority) {
        viewModelScope.launch {
            dataStoreRepository.saveSortState(priority)
        }
    }

    fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map {
                        Priority.valueOf(it)
                    }.collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

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

    fun getSearchedTasks(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading // While loading from database
        try {
            viewModelScope.launch {
                repository.searchTasks(searchQuery).collect {
                    _searchedTasks.value = RequestState.Success(it) // When collected all data
                }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e) // If error occurs
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
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
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }


    fun updateTitle(newTitle: String) {
        if (newTitle.length <= Constants.MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }


    fun validateFields(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }


    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(
                title = title,
                description = description,
                priority = priority
            )

            repository.addTask(task)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(
                id = id,
                title = title,
                description = description,
                priority = priority
            )

            repository.updateTask(task)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(
                id = id,
                title = title,
                description = description,
                priority = priority
            )

            repository.deleteTask(task)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun updateAction(action: Action) {
        this.action = action
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updatePriority(priority: Priority) {
        this.priority = priority
    }

    fun updateSearchAppBarState(state: SearchAppBarState) {
        this.searchAppBarState = state
    }

    fun updateSearchTextState(state: String) {
        this.searchTextState = state
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> {
                updateTask()
            }

            Action.DELETE -> {
                deleteTask()
            }

            Action.DELETE_ALL -> {
                deleteAllTasks()
            }

            Action.UNDO -> {
                addTask()
            }

            else -> {

            }
        }


    }


}