package com.ucb.app.github.data.service

import com.ucb.app.github.data.dto.GitHubUserDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class GitHubService(
    private val httpClient: HttpClient
) {
    suspend fun getUser(username: String): GitHubUserDto {
        return httpClient.get("https://api.github.com/users/$username") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}
