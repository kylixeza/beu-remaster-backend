package util

import io.ktor.server.application.*

fun ApplicationCall.getPreferConsumeAt(): PreferConsumeAt {
    val currentTime = getDateTimeBasedOnIp()

    return when (currentTime.hour) {
        in 5..9 -> PreferConsumeAt.BREAKFAST
        in 10..14 -> PreferConsumeAt.LUNCH
        in 15..17 -> PreferConsumeAt.SNACK
        in 18..24 -> PreferConsumeAt.DINNER
        in 0..4 -> PreferConsumeAt.DINNER
        else -> PreferConsumeAt.SNACK
    }
}