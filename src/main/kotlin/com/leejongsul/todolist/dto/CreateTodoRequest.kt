package com.leejongsul.todolist.dto

data class CreateTodoRequest(
    val contents: String,
    val status: String
)
