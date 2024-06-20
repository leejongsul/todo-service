package com.leejongsul.todolist.entities

import jakarta.persistence.*

@Table(name = "users")
@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val nickname: String,
    var password: String,
    var accessToken: String? = null,
    var refreshToken: String? = null,
) {
    fun getId(): Long = id!!
}