package com.ucb.app.github.data.mapper

import com.ucb.app.github.data.dto.GitHubUserDto
import com.ucb.app.github.domain.model.GitHubUserModel

fun GitHubUserDto.toModel(): GitHubUserModel {
    return GitHubUserModel(
        login = login,
        name = name ?: "Sin nombre",
        avatarUrl = avatarUrl,
        bio = bio ?: "Sin biografía",
        publicRepos = publicRepos,
        followers = followers,
        following = following
    )
}
