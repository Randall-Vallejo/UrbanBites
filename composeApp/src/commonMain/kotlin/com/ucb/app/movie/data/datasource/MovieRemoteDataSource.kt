package com.ucb.app.movie.data.datasource

import com.ucb.app.movie.data.dto.DiscoverMovieResponseDto
import com.ucb.app.movie.data.service.MovieService

class MovieRemoteDataSource(
    private val service: MovieService
) {
    suspend fun discoverPopularMovies(apiKey: String): DiscoverMovieResponseDto {
        return service.discoverPopularMovies(apiKey)
    }
}
