package com.leejongsul.todolist.services

import com.leejongsul.todolist.repositories.UserRepository
import com.leejongsul.todolist.security.CustomUserDetails
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): CustomUserDetails {
        val user = userRepository.findByNickname(username) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "회원을 찾을 수 없습니다. nickname=$username"
        )
        return CustomUserDetails(user)
    }
}