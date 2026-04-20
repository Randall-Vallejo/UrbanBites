package com.ucb.app.movie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.movie.domain.usecase.GetPopularMoviesUseCase
import com.ucb.app.movie.presentation.state.MovieState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MovieState())
    val state: StateFlow<MovieState> = _state.asStateFlow()

    fun loadMovies(apiKey: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                movies = emptyList()
            )

            try {
                val result = getPopularMoviesUseCase(apiKey)
                _state.value = _state.value.copy(
                    isLoading = false,
                    movies = result,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    movies = emptyList(),
                    error = "Ocurrió un error: ${e.message}"
                )
            }
        }
    }
}
