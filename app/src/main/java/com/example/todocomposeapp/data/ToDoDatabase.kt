package com.example.todocomposeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todocomposeapp.data.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun toDoDao(): ToDoDao
}