package com.ucb.app.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {

    @Serializable
    data object Profile : NavRoute()

    @Serializable
    data object ProfileEdit : NavRoute()

    @Serializable
    data object GitHub : NavRoute()

    @Serializable
    data object Movie : NavRoute()
}
