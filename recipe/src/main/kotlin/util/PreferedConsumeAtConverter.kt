package util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getPreferConsumeAt(): PreferConsumeAt {
    val now = Clock.System.now()
    val currentTime = now.toLocalDateTime(TimeZone.currentSystemDefault()).time
    return when (currentTime) {
        in LocalTime(5, 0)..LocalTime(10, 0) -> PreferConsumeAt.BREAKFAST
        in LocalTime(10, 0)..LocalTime(14, 0) -> PreferConsumeAt.LUNCH
        in LocalTime(14, 0)..LocalTime(19, 0) -> PreferConsumeAt.SNACK
        in LocalTime(19, 0)..LocalTime(5, 0) -> PreferConsumeAt.DINNER
        else -> PreferConsumeAt.SNACK
    }
}