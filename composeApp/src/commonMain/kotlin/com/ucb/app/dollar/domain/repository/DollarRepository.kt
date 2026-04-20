package com.ucb.app.dollar.domain.repository

import com.ucb.app.dollar.domain.model.DollarModel

interface DollarRepository {
    suspend fun save(model: DollarModel)
    suspend fun getList(): List<DollarModel>
}
