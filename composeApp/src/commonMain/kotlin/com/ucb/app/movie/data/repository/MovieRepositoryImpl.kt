package com.ucb.app.movie.data.repository

import com.ucb.app.movie.data.datasource.MovieRemoteDataSource
import com.ucb.app.movie.data.mapper.toModel
import com.ucb.app.movie.domain.model.MovieModel
import com.ucb.app.movie.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val remoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override suspend fun discoverPopularMovies(apiKey: String): List<MovieModel> {
        return remoteDataSource
            .discoverPopularMovies(apiKey)
            .results
            .map { it.toModel() }
    }
}
