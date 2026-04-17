package com.ucb.app.github.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubUserDto(
    @SerialName("login")
    val login: String,

    @SerialName("name")
    val name: String? = null,

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("bio")
    val bio: String? = null,

    @SerialName("public_repos")
    val publicRepos: Int = 0,

    @SerialName("followers")
    val followers: Int = 0,

    @SerialName("following")
    val following: Int = 0
)
