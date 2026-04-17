package com.ucb.app.github.data.datasource

import com.ucb.app.github.data.dto.GitHubUserDto
import com.ucb.app.github.data.service.GitHubService

class GitHubRemoteDataSource(
    private val service: GitHubService
) {
    suspend fun getUser(username: String): GitHubUserDto {
        return service.getUser(username)
    }
}
