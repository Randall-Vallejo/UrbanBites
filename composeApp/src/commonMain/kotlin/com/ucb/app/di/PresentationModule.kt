package com.ucb.app.di

import com.ucb.app.cart.presentation.viewmodel.CartViewModel
import com.ucb.app.crypto.presentation.viewmodel.CryptoViewModel
import com.ucb.app.firebase.presentation.viewmodel.NotificationViewModel
import com.ucb.app.github.presentation.viewmodel.GitHubViewModel
import com.ucb.app.movie.presentation.viewmodel.MovieViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::GitHubViewModel)
    viewModelOf(::MovieViewModel)
    viewModelOf(::CryptoViewModel)
    viewModelOf(::NotificationViewModel)
    viewModel { CartViewModel(get()) }
}
