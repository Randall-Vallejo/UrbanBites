package com.ucb.app.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavRoute {

    @Serializable
    object Login: NavRoute()

    @Serializable
    object Register: NavRoute()

    @Serializable
    object Home: NavRoute()

    @Serializable
    object Map: NavRoute()

    @Serializable
    object Favorites: NavRoute()

    @Serializable
    object Profile: NavRoute()

    @Serializable
    object NotificationSettings: NavRoute()

    @Serializable
    object Settings: NavRoute()

    @Serializable
    object Language: NavRoute()

    @Serializable
    data class Detail(val truckName: String): NavRoute()

    @Serializable
    object Demo: NavRoute()

    @Serializable
    object AddFoodTruck: NavRoute()
}
