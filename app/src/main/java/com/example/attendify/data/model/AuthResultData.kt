package com.example.attendify.data.model

data class AuthResultData(
    val isNewUser: Boolean,
    val displayName: String?,
    val email: String?,
    val uid: String?
)