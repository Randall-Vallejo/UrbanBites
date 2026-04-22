package com.ucb.app.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavRoute {


    @Serializable
    object Profile: NavRoute()


    @Serializable
    object ProfileEdit: NavRoute()

    @Serializable
    object Github: NavRoute()

    @Serializable
    object Movies: NavRoute()

    @Serializable
    object Crypto: NavRoute()

    @Serializable
    object FakeStore: NavRoute()

    @Serializable
    object CountryStore: NavRoute()

    @Serializable
    object Maps: NavRoute()

    @Serializable
    object Notifications: NavRoute()

    @Serializable
    object Dollar: NavRoute()
}
