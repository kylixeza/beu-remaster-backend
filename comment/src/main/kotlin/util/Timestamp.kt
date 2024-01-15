package util

import kotlinx.datetime.*

fun createTimeStamp() = run {
    val now = Clock.System.now()
   now.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.durationSince(): String = run {
    val now = Clock.System.now()
    val period = toInstant(TimeZone.currentSystemDefault())
        .periodUntil(now, TimeZone.currentSystemDefault())

    val usingSecondUnit = period.seconds in 1..59 && period.minutes == 0
    val usingMinuteUnit = period.minutes in 1..59 && period.hours == 0
    val usingHourUnit = period.hours in 1..23 && period.days == 0
    val usingDayUnit = period.days in 1..6 && period.months == 0
    val usingWeekUnit = period.days in 7..30 && period.months == 0
    val usingMonthUnit = period.months in 1..11 && period.years == 0

    when {
        usingSecondUnit -> "${period.seconds} detik yang lalu"
        usingMinuteUnit -> "${period.minutes} menit yang lalu"
        usingHourUnit -> "${period.hours} jam yang lalu"
        usingDayUnit -> "${period.days} hari yang lalu"
        usingWeekUnit -> "${period.days / 7} minggu yang lalu"
        usingMonthUnit -> "${period.months} bulan yang lalu"
        else -> "$dayOfMonth-$monthNumber-$year"
    }
}