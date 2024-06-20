package com.leejongsul.todolist.controllers

import com.leejongsul.todolist.dto.AuthenticationRequest
import com.leejongsul.todolist.dto.AuthenticationResponse
import com.leejongsul.todolist.dto.RefreshTokenRequest
import com.leejongsul.todolist.services.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
) {
    @PostMapping
    fun authenticate(
        @RequestBody authenticationRequest: AuthenticationRequest
    ): AuthenticationResponse {
        return authenticationService.authentication(authenticationRequest)
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): AuthenticationResponse {
        return authenticationService.refreshAccessToken(request.token)
    }
}