package com.leejongsul.todolist.controllers

import com.leejongsul.todolist.dto.CreateTodoRequest
import com.leejongsul.todolist.dto.UpdateTodoStatusRequest
import com.leejongsul.todolist.entities.Todo
import com.leejongsul.todolist.security.CustomUserDetails
import com.leejongsul.todolist.services.TodoService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todo")
class TodoController(
    private val todoService: TodoService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@AuthenticationPrincipal userDetails: CustomUserDetails, @RequestBody request: CreateTodoRequest) {
        todoService.create(userDetails, request)
    }

    @PutMapping("/{id}")
    fun updateStatus(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable id: Long,
        @RequestBody request: UpdateTodoStatusRequest
    ) {
        todoService.updateStatus(userDetails, id, request)
    }

    @GetMapping("/latest")
    fun getLatest(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): Todo {
        return todoService.getLatest(userDetails)
    }

    @GetMapping("/list")
    fun getList(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): List<Todo> {
        return todoService.getTodoList(userDetails)
    }
}