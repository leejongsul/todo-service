package com.leejongsul.todolist.repositories

import com.leejongsul.todolist.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByNickname(nickname: String): User?
    fun findByRefreshToken(refreshToken: String): User?
}