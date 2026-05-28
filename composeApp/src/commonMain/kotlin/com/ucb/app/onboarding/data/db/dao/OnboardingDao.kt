package com.ucb.app.onboarding.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.app.onboarding.data.db.OnboardingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OnboardingDao {
    @Query("SELECT isCompleted FROM onboarding_prefs WHERE id = 1")
    fun isOnboardingCompleted(): Flow<Boolean?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setOnboardingCompleted(prefs: OnboardingEntity)
}
