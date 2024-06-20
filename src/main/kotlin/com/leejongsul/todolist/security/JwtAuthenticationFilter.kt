package com.leejongsul.todolist.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    val log = KotlinLogging.logger { }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader == null) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authorizationHeader.substring("Bearer ".length)

        try {
            if (!jwtTokenProvider.isExpired(token) && SecurityContextHolder.getContext().authentication == null) {
                val authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
                log.info { "Jwt Token 인증 성공" }
            }
        } catch (ex: Exception) {
            request.setAttribute("exception", ex)
            log.info { "Jwt Token 인증 실패" }
        }

        filterChain.doFilter(request, response)
    }
}