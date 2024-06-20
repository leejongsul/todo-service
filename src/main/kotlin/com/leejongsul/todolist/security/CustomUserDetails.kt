package com.leejongsul.todolist.security

import com.leejongsul.todolist.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val user: User
) : UserDetails {
    fun getUserId(): Long = user.getId()
    override fun getUsername(): String = user.nickname
    override fun getPassword(): String = user.password
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

}