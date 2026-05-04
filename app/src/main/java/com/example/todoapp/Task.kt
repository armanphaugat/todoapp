package com.example.todoapp

import android.R

data class Task(
    var id: Int=0,
    val name: String,
    var isCompleted: Boolean=false
)
