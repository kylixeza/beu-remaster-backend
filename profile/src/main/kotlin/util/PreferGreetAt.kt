package util

import io.ktor.server.application.*

fun ApplicationCall.getPreferGreetAt(): String {
    val currentTime = getDateTimeBasedOnIp()

    return when (currentTime.hour) {
        in 5..9 -> "Selamat pagi"
        in 10..14 -> "Selamat siang"
        in 15..17 -> "Selamat sore"
        in 18..24 -> "Selamat malam"
        in 0..4 -> "Selamat malam"
        else -> "Selamat datang"
    }
}