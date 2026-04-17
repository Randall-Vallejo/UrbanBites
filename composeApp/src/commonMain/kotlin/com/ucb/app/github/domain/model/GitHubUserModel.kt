package com.ucb.app.github.domain.model

data class GitHubUserModel(
    val login: String,
    val name: String,
    val avatarUrl: String,
    val bio: String,
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)
