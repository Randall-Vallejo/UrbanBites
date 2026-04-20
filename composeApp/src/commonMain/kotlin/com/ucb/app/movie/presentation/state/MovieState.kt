package com.ucb.app.movie.presentation.state

import com.ucb.app.movie.domain.model.MovieModel

data class MovieState(
    val isLoading: Boolean = false,
    val movies: List<MovieModel> = emptyList(),
    val error: String? = null
)
