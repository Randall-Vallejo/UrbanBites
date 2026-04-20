package com.ucb.app.dollar.presentation.state

import com.ucb.app.dollar.domain.model.DollarModel

data class DollarUIState(
    val list: List<DollarModel> = emptyList(),
    val isLoading: Boolean = true
)
