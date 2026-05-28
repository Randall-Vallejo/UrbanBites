package com.ucb.app.onboarding.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OnboardingConfig(
    val onboarding_config: List<OnboardingItem>
)

@Serializable
data class OnboardingItem(
    val id: Int,
    val title: Map<String, String>,
    val description: Map<String, String>,
    val image_url: Map<String, String>
)
