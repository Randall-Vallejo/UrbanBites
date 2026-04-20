package com.ucb.app.dollar.domain.usecase

import com.ucb.app.dollar.domain.model.DollarModel
import com.ucb.app.dollar.domain.repository.DollarRepository

class CreateDollarUseCase(
    private val repository: DollarRepository
) {

    suspend fun invoke(model: DollarModel) {
        repository.save(model)
    }
}
