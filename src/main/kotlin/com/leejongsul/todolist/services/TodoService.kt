package com.leejongsul.todolist.services

import com.leejongsul.todolist.dto.CreateTodoRequest
import com.leejongsul.todolist.dto.UpdateTodoStatusRequest
import com.leejongsul.todolist.entities.Todo
import com.leejongsul.todolist.repositories.TodoRepository
import com.leejongsul.todolist.security.CustomUserDetails
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TodoService(
    private val todoRepository: TodoRepository
) {
    val logger = KotlinLogging.logger { }

    @Transactional
    fun create(userDetails: CustomUserDetails, request: CreateTodoRequest): Todo {
        return todoRepository.save(
            Todo(
                userId = userDetails.getUserId(),
                contents = request.contents,
                status = request.status
            )
        ).also {
            logger.info { "회원[${userDetails.getUserId()}] 할 일[${it.id}]을 생성했습니다." }
        }
    }

    @Transactional
    fun updateStatus(userDetails: CustomUserDetails, id: Long, request: UpdateTodoStatusRequest) {
        val todo = todoRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "할 일($id)을 찾을 수 없습니다."
        )
        val oldStatus = todo.status

        if (request.status == "대기") {
            if (todo.status != "진행 중") throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "진행 중 상태에서만 대기 상태로 변경될 수 있습니다."
            )
            todo.status = request.status
        } else todo.status = request.status

        logger.info { "회원[${userDetails.getUserId()}]이 할 일[${todo.id}]의 상태를 [$oldStatus]-> [${request.status}] 변경했습니다." }
    }

    fun getTodoList(userDetails: CustomUserDetails): List<Todo> {
        return todoRepository.findAllByUserId(userDetails.getUserId()).also {
            logger.info { "할 일 목록을 가져 왔습니다." }
        }
    }

    fun getLatest(userDetails: CustomUserDetails): Todo {
        return todoRepository.findTopByUserIdOrderByIdDesc(userDetails.getUserId()) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "가장 최근의 할 일을 찾을 수 없습니다."
        )
    }
}