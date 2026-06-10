package com.ucb.app.di

import com.ucb.app.demo.presentation.viewmodel.DemoViewModel
import com.ucb.app.home.presentation.viewmodel.AddFoodTruckViewModel
import com.ucb.app.home.presentation.viewmodel.HomeViewModel
import com.ucb.app.login.presentation.viewmodel.LoginViewModel
import com.ucb.app.login.presentation.viewmodel.RegisterViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::DemoViewModel)
    viewModelOf(::AddFoodTruckViewModel)
    
    // Registro manual para resolver la inyección de NotificationProvider (Interface)
    viewModel { HomeViewModel(get(), get(), get()) }
}
