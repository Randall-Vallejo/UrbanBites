package com.ucb.app.movie.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ucb.app.movie.presentation.viewmodel.MovieViewModel
import org.koin.compose.viewmodel.koinViewModel

private const val TMDB_API_KEY = "fa3e844ce31744388e07fa47c7c5d8c3"

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMovies(TMDB_API_KEY)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Películas Populares", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = { viewModel.loadMovies(TMDB_API_KEY) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Refrescar")
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }

        state.error?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.movies) { movie ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = movie.posterUrl,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .width(100.dp)
                                .height(150.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Fecha: ${movie.releaseDate}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Rating: ${movie.voteAverage}", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 3
                            )
                        }
                    }
                }
            }
        }
    }
}
