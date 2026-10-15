package com.example.exp10

import java.io.Serializable

data class Contact(
    val name: String,
    val number: String,
    val email: String = ""
) : Serializable {
    val initial: String
        get() = name.trim().firstOrNull()?.uppercase() ?: "?"
}