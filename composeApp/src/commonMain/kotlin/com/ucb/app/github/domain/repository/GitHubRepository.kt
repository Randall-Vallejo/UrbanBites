package com.ucb.app.github.domain.repository

import com.ucb.app.github.domain.model.GitHubUserModel

interface GitHubRepository {
    suspend fun getUser(username: String): GitHubUserModel
}
