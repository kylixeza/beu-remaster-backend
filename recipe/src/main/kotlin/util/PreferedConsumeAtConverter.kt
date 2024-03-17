package util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId

fun getPreferConsumeAt(): PreferConsumeAt {
    val now = Clock.System.now()
    //TODO: Make sure to change the timezone to the user's timezone using global ip address to detect the timezone
    val currentTime = now.toLocalDateTime(TimeZone.of("Asia/Jakarta")).time
    return when (currentTime.hour) {
        in 5..9 -> PreferConsumeAt.BREAKFAST
        in 10..14 -> PreferConsumeAt.LUNCH
        in 15..17 -> PreferConsumeAt.SNACK
        in 18..24 -> PreferConsumeAt.DINNER
        in 0..4 -> PreferConsumeAt.DINNER
        else -> PreferConsumeAt.SNACK
    }
}