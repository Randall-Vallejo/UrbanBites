package com.ucb.app.firebase.data.datasource

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

actual class RemoteConfigManager actual constructor() {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        // Configuramos el tiempo de recarga (0 segundos para desarrollo/pruebas)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Valores por defecto
        remoteConfig.setDefaultsAsync(mapOf(
            "welcome_message" to "Bienvenido a UrbanBites (Local)",
            "onboarding_config" to """
                [
                  {
                    "id": 1,
                    "title": { "es": "¡Organiza tu día!", "en": "Organize your day!", "fr": "Organisez votre journée !" },
                    "description": { "es": "Gestiona tareas, proyectos y prioridades de forma sencilla con FlowWise.", "en": "Easily manage tasks, projects, and priorities with FlowWise.", "fr": "Gérez facilement vos tâches, projets et priorités avec FlowWise." },
                    "image_url": { "es": "https://raw.githubusercontent.com/randall-sv/UrbanBites/main/assets/onboarding1.png", "en": "https://raw.githubusercontent.com/randall-sv/UrbanBites/main/assets/onboarding1.png", "fr": "https://raw.githubusercontent.com/randall-sv/UrbanBites/main/assets/onboarding1.png" }
                  },
                  {
                    "id": 2,
                    "title": { "es": "Trabaja en equipo", "en": "Teamwork", "fr": "Travail d'équipe" },
                    "description": { "es": "Colabora en tiempo real con tus compañeros y mantén a todos sincronizados.", "en": "Collaborate in real-time with your teammates and keep everyone in sync.", "fr": "Collaborez en temps réel con vos collègues et gardez tout le monde synchronisé." },
                    "image_url": { "es": "https://raw.githubusercontent.com/randall-sv/UrbanBites/main/assets/onboarding2.png", "en": "https://raw.githubusercontent.com/randall-sv/UrbanBites/main/assets/onboarding2.png", "fr": "https://raw.githubusercontent.com/randall-sv/UrbanBites/main/assets/onboarding2.png" }
                  }
                ]
            """.trimIndent()
        ))

        // Intentar traer datos de la nube inmediatamente
        remoteConfig.fetchAndActivate()
    }

    // Función genérica de Randall
    actual suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            false
        }
    }

    // Función genérica de Randall
    actual fun getString(key: String): String {
        val value = remoteConfig.getString(key)
        // Evitamos devolver "Cargando..." si hay un valor por defecto válido
        return if (value.isEmpty() && !remoteConfig.getKeysByPrefix("").contains(key)) "Cargando..." else value
    }

    // Función específica de Huayna
    actual fun getWelcomeMessage(): String {
        return remoteConfig.getString("welcome_message")
    }
}
