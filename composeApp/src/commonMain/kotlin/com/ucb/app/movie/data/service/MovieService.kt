package com.ucb.app.movie.data.service

import com.ucb.app.movie.data.dto.DiscoverMovieResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MovieService(
    private val httpClient: HttpClient
) {
    suspend fun discoverPopularMovies(apiKey: String): DiscoverMovieResponseDto {
        // Ignoramos el parámetro apiKey y usamos la URL hardcodeada como se solicitó
        return httpClient.get("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=fa3e844ce31744388e07fa47c7c5d8c3")
            .body()
    }
}
