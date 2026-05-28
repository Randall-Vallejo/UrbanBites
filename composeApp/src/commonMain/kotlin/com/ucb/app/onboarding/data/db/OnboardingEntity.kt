package com.ucb.app.onboarding.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "onboarding_prefs")
data class OnboardingEntity(
    @PrimaryKey val id: Int = 1,
    val isCompleted: Boolean = false
)
