package com.ucb.app.github.data.repository

import com.ucb.app.github.data.datasource.GitHubRemoteDataSource
import com.ucb.app.github.data.mapper.toModel
import com.ucb.app.github.domain.model.GitHubUserModel
import com.ucb.app.github.domain.repository.GitHubRepository

class GitHubRepositoryImpl(
    private val remoteDataSource: GitHubRemoteDataSource
) : GitHubRepository {

    override suspend fun getUser(username: String): GitHubUserModel {
        return remoteDataSource.getUser(username).toModel()
    }
}
