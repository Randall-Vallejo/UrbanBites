package com.ucb.app.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSession {
    private val _userName = MutableStateFlow("Invitado")
    val userName = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("invitado@urbanbites.com")
    val userEmail = _userEmail.asStateFlow()

    fun updateSession(name: String, email: String) {
        _userName.value = name
        _userEmail.value = email
    }
}
