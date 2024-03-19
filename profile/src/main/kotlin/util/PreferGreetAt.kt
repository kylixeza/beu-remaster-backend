package util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getPreferGreetAt(): String {
    val now = Clock.System.now()
    //TODO: Make sure to change the timezone to the user's timezone using global ip address to detect the timezone
    val currentTime = now.toLocalDateTime(TimeZone.of("Asia/Jakarta")).time
    return when (currentTime.hour) {
        in 5..9 -> "Selamat pagi"
        in 10..14 -> "Selamat siang"
        in 15..17 -> "Selamat sore"
        in 18..24 -> "Selamat malam"
        in 0..4 -> "Selamat malam"
        else -> "Selamat datang"
    }
}