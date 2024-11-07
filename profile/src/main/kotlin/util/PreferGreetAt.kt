package util

import io.ktor.server.application.*

fun ApplicationCall.getPreferGreetAt(): String {
    val currentTime = getDateTimeBasedOnIp()

    return when (currentTime.hour) {
        in 5..9 -> "Good morning"
        in 10..14 -> "Good afternoon"
        in 15..21 -> "Good evening"
        in 22..24 -> "Good night"
        in 0..4 -> "Good night"
        else -> "Welcome"
    }
}