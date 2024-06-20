package com.leejongsul.todolist.controllers

import com.leejongsul.todolist.dto.SignUpRequest
import com.leejongsul.todolist.security.CustomUserDetails
import com.leejongsul.todolist.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody request: SignUpRequest) {
        return userService.signUp(request)
    }

    @DeleteMapping("/withdraw")
    fun withdraw(@AuthenticationPrincipal userDetails: CustomUserDetails) {
        userService.withdraw(userDetails.getUserId())
    }
}