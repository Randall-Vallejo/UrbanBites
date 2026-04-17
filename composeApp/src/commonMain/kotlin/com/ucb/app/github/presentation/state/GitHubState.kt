package com.ucb.app.github.presentation.state

import com.ucb.app.github.domain.model.GitHubUserModel

data class GitHubState(
    val username: String = "",
    val isLoading: Boolean = false,
    val user: GitHubUserModel? = null,
    val error: String? = null
)
