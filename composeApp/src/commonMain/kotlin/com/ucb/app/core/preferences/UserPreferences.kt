package com.ucb.app.core.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class DistanceUnit { KM, MILES }
enum class AppTheme { LIGHT, DARK, SYSTEM }

object UserPreferences {
    // Notificaciones
    private val _fcmEnabled = MutableStateFlow(true)
    val fcmEnabled = _fcmEnabled.asStateFlow()

    private val _proximityAlertsEnabled = MutableStateFlow(true)
    val proximityAlertsEnabled = _proximityAlertsEnabled.asStateFlow()

    private val _localFavoritesEnabled = MutableStateFlow(true)
    val localFavoritesEnabled = _localFavoritesEnabled.asStateFlow()

    // Configuración
    private val _distanceUnit = MutableStateFlow(DistanceUnit.KM)
    val distanceUnit = _distanceUnit.asStateFlow()

    // Requerimiento 6: Tema Claro por defecto
    private val _appTheme = MutableStateFlow(AppTheme.LIGHT)
    val appTheme = _appTheme.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("Español")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    // Métodos de actualización
    fun setFcmEnabled(enabled: Boolean) { _fcmEnabled.value = enabled }
    fun setProximityEnabled(enabled: Boolean) { _proximityAlertsEnabled.value = enabled }
    fun setLocalEnabled(enabled: Boolean) { _localFavoritesEnabled.value = enabled }
    fun setDistanceUnit(unit: DistanceUnit) { _distanceUnit.value = unit }
    fun setTheme(theme: AppTheme) { _appTheme.value = theme }
    fun setLanguage(lang: String) { _selectedLanguage.value = lang }
}
