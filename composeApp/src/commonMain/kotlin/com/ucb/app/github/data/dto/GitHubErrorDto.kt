package com.ucb.app.github.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubErrorDto(
    @SerialName("message")
    val message: String,
    @SerialName("documentation_url")
    val documentationUrl: String? = null
)
