package com.ucb.app.core.util

import kotlin.math.round

fun Double.toOneDecimal(): String {
    return (round(this * 10) / 10.0).toString()
}
