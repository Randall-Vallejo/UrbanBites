package com.ucb.app.onboarding.presentation.state

import com.ucb.app.onboarding.data.model.OnboardingItem

data class OnboardingUiState(
    val items: List<OnboardingItem> = emptyList(),
    val currentPage: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)
