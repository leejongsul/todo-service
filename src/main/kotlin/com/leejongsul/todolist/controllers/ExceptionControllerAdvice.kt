package com.leejongsul.todolist.controllers

import com.leejongsul.todolist.exceptions.RefreshTokenException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException


@RestControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("토큰이 유효하지 않습니다."), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("올바르지 않은 토큰입니다."), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(ex: ExpiredJwtException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("토큰이 만료되었습니다."), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(value = [RefreshTokenException::class, InsufficientAuthenticationException::class])
    fun handleUsersException(ex: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(ex.message), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleUsersException(ex: ResponseStatusException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(ex.reason), ex.statusCode)
    }

    @ExceptionHandler
    fun handleException(ex: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(ex.message), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    class ErrorMessage(val message: String? = null)
}