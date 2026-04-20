package com.ucb.app.dollar.data.repository

import com.ucb.app.dollar.data.datasource.DollarLocalDataSource
import com.ucb.app.dollar.data.mapper.toEntity
import com.ucb.app.dollar.data.mapper.toModel
import com.ucb.app.dollar.domain.model.DollarModel
import com.ucb.app.dollar.domain.repository.DollarRepository

class DollarRepositoryImpl(
    val localDataSource: DollarLocalDataSource
): DollarRepository {
    override suspend fun save(model: DollarModel) {
        localDataSource.save(model.toEntity())
    }

    override suspend fun getList(): List<DollarModel> {
        return localDataSource.list().map {
            it.toModel()
        }
    }
}
