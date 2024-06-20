package com.leejongsul.todolist.services

import com.leejongsul.todolist.dto.SignUpRequest
import com.leejongsul.todolist.entities.User
import com.leejongsul.todolist.repositories.UserRepository
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    val logger = KotlinLogging.logger { }

    @Transactional
    fun signUp(signUpRequest: SignUpRequest) {
        val found = userRepository.findByNickname(signUpRequest.nickname)

        if (found != null) {
            logger.info { "이미 존재하는 닉네임 입니다." }
            throw ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다.")
        }
        val user = User(
            nickname = signUpRequest.nickname,
            password = passwordEncoder.encode(signUpRequest.password)
        )
        userRepository.save(user).also {
            logger.info { "회원 생성 완료. id=${it.id}, nickname=${it.nickname}" }
        }
    }

    @Transactional
    fun withdraw(id: Long) {
        userRepository.deleteById(id)
        logger.info { "회원 삭제 완료. id=${id}" }
    }
}