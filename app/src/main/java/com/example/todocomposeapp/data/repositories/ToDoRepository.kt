package com.example.todocomposeapp.data.repositories

import com.example.todocomposeapp.data.ToDoDao
import com.example.todocomposeapp.data.models.Task
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    val getAllTasks: Flow<List<Task>> = toDoDao.getAllallTask()
    val sortedByLowPriority: Flow<List<Task>> = toDoDao.sortByLowPriority()
    val sortedByHighPriority: Flow<List<Task>> = toDoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<Task> = toDoDao.getTaskById(taskId)
    fun searchTasks(searchQuery: String) = toDoDao.searchForTasks(searchQuery)
    fun addTask(task: Task) = toDoDao.addTask(task)
    fun updateTask(task: Task) = toDoDao.updateTask(task)
    fun deleteTask(task: Task) = toDoDao.deleteTask(task)
    fun deleteAllTasks() = toDoDao.deleteAllTasks()


}