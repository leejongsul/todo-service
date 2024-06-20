package com.leejongsul.todolist.entities

import jakarta.persistence.*

@Table(name = "todos")
@Entity
class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val userId: Long,
    var status: String,
    var contents: String
)