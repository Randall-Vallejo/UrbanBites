package com.ucb.app.movie.domain.model

data class MovieModel(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val overview: String,
    val voteAverage: Double,
    val releaseDate: String
)
