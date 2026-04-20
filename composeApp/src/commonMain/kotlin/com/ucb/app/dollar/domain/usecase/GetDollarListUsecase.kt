package com.ucb.app.dollar.domain.usecase

import com.ucb.app.dollar.domain.model.DollarModel
import com.ucb.app.dollar.domain.repository.DollarRepository
import kotlinx.coroutines.delay

class GetDollarListUsecase(
    val repository: DollarRepository
) {

    suspend fun invoke(): List<DollarModel> {
        return repository.getList()
    }
}
