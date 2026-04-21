package com.ucb.app.login.data.repository

import com.ucb.app.login.domain.model.LoginModel
import com.ucb.app.login.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override suspend fun login(model: LoginModel) {
        // Implementación básica por ahora
        println("Login attempt with: ${model.email}")
    }
}
