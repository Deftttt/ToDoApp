package com.example.todocomposeapp.data.models

import androidx.compose.ui.graphics.Color
import com.example.todocomposeapp.ui.theme.HighPriorityColor
import com.example.todocomposeapp.ui.theme.LowPriorityColor
import com.example.todocomposeapp.ui.theme.MediumPriorityColor
import com.example.todocomposeapp.ui.theme.NonePriorityColor


enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}