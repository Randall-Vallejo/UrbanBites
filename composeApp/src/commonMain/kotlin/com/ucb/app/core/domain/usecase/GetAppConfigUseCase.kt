package com.ucb.app.core.domain.usecase

import com.ucb.app.core.domain.repository.ConfigRepository

class GetAppConfigUseCase(
    private val repository: ConfigRepository
) {
    suspend operator fun invoke(key: String): String {
        return repository.getAppConfig(key)
    }
}
