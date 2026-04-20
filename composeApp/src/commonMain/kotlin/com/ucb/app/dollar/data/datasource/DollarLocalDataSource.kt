package com.ucb.app.dollar.data.datasource

import com.ucb.app.dollar.data.entity.DollarEntity

interface DollarLocalDataSource {
    suspend fun save(entity: DollarEntity)
    suspend fun list() : List<DollarEntity>
}
