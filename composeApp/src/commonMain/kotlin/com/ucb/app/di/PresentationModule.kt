package com.ucb.app.di

import com.ucb.app.github.presentation.viewmodel.GitHubViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::GitHubViewModel)
}
