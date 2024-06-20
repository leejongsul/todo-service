package com.leejongsul.todolist.services

import com.leejongsul.todolist.dto.AuthenticationRequest
import com.leejongsul.todolist.dto.AuthenticationResponse
import com.leejongsul.todolist.exceptions.RefreshTokenException
import com.leejongsul.todolist.repositories.UserRepository
import com.leejongsul.todolist.security.CustomUserDetails
import com.leejongsul.todolist.security.JwtTokenProvider
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
) {
    val logger = KotlinLogging.logger { }

    @Transactional
    fun authentication(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        val authentication = authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.nickname,
                authenticationRequest.password
            )
        ).also {
            logger.info { "인증 성공" }
        }

        val user = (authentication.principal as CustomUserDetails).user

        return AuthenticationResponse(
            accessToken = createAccessToken(user.nickname),
            refreshToken = createRefreshToken(user.nickname)
        ).also {
            user.accessToken = it.accessToken
            user.refreshToken = it.refreshToken
        }
    }

    fun refreshAccessToken(refreshToken: String): AuthenticationResponse {
        val user = userRepository.findByRefreshToken(refreshToken) ?: throw RefreshTokenException("회원을 찾을 수 없습니다.")
        if (jwtTokenProvider.isExpired(refreshToken)) {
            throw throw RefreshTokenException("Refresh Token 만료")
        }
        return AuthenticationResponse(accessToken = createAccessToken(user.nickname)).also {
            user.accessToken = it.accessToken
        }
    }

    private fun createAccessToken(nickname: String) = jwtTokenProvider.generate(
        nickname = nickname,
        expiredAt = Date().apply { time += 1000L * 60 * 10 }
    )

    private fun createRefreshToken(nickname: String) = jwtTokenProvider.generate(
        nickname = nickname,
        expiredAt = Date().apply { time += 1000L * 60 * 10 * 24 }
    )
}