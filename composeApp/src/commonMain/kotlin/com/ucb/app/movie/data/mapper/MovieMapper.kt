package com.ucb.app.movie.data.mapper

import com.ucb.app.movie.data.dto.MovieDto
import com.ucb.app.movie.domain.model.MovieModel

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185"

fun MovieDto.toModel(): MovieModel {
    return MovieModel(
        id = id,
        title = title,
        posterUrl = if (posterPath != null) "$IMAGE_BASE_URL$posterPath" else "",
        overview = overview,
        voteAverage = voteAverage,
        releaseDate = releaseDate
    )
}
