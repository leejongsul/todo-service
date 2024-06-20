package com.leejongsul.todolist.repositories

import com.leejongsul.todolist.entities.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long> {
    fun findAllByUserId(userId: Long): List<Todo>
    fun findTopByUserIdOrderByIdDesc(userId: Long): Todo?
}