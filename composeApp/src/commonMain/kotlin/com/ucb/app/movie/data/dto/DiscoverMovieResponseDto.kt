package com.ucb.app.movie.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverMovieResponseDto(
    val page: Int = 1,

    @SerialName("results")
    val results: List<MovieDto> = emptyList()
)
