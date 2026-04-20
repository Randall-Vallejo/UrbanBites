package com.ucb.app.movie.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,

    @SerialName("poster_path")
    val posterPath: String? = null,

    @SerialName("backdrop_path")
    val backdropPath: String? = null,

    @SerialName("overview")
    val overview: String = "",

    @SerialName("vote_average")
    val voteAverage: Double = 0.0,

    @SerialName("release_date")
    val releaseDate: String = ""
)
