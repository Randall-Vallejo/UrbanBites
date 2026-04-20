package com.ucb.app.dollar.data.service

import com.ucb.app.dollar.data.dao.DollarDao
import com.ucb.app.dollar.data.datasource.DollarLocalDataSource
import com.ucb.app.dollar.data.entity.DollarEntity

class DbService(val dollarDao: DollarDao): DollarLocalDataSource {
    override suspend fun save(entity: DollarEntity) {
        dollarDao.insert(entity)
    }

    override suspend fun list(): List<DollarEntity> {
        return dollarDao.getList()
    }
}
