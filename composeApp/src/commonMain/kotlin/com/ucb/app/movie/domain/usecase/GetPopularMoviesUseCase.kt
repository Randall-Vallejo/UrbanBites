package com.ucb.app.movie.domain.usecase

import com.ucb.app.movie.domain.model.MovieModel
import com.ucb.app.movie.domain.repository.MovieRepository

class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(apiKey: String): List<MovieModel> {
        return repository.discoverPopularMovies(apiKey)
    }
}
