package com.ucb.app.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavRoute {

    @Serializable
    object Login: NavRoute()

    @Serializable
    object Home: NavRoute()

    @Serializable
    object Demo: NavRoute()
}
