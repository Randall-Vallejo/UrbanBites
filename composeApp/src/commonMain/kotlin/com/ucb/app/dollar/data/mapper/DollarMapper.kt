package com.ucb.app.dollar.data.mapper

import com.ucb.app.dollar.data.entity.DollarEntity
import com.ucb.app.dollar.domain.model.DollarModel

fun DollarModel.toEntity() = DollarEntity(
    dollarOfficial,
    dollarParallel
)

fun DollarEntity.toModel() =  DollarModel(
    id,
    dollarOfficial ?: "",
    dollarParallel ?: ""
)
