package com.example.exp9

data class User(
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val username: String,
    val passwordHash: String = "",
    val createdAt: String = ""
)
