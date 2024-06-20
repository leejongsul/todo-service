package com.leejongsul.todolist.dto

data class AuthenticationResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
)
