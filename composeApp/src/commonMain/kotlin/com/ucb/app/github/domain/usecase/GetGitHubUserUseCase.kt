package com.ucb.app.github.domain.usecase

import com.ucb.app.github.domain.model.GitHubUserModel
import com.ucb.app.github.domain.repository.GitHubRepository

class GetGitHubUserUseCase(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(username: String): GitHubUserModel {
        return repository.getUser(username)
    }
}
