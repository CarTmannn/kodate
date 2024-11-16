package com.example.kodate.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val profilePic: String = "",
    val displayPic: String = "",
    val age: Int? = null,
    val city: String = "",
    val gender: String = "",
    val interests: List<String> = emptyList(),
    val likedUsers: List<String> = emptyList(),
    val matchedUsers: List<String> = emptyList(),
    val bio: String = "",
)
