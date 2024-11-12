package util

import io.ipgeolocation.api.GeolocationParams
import io.ipgeolocation.api.IPGeolocationAPI
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.datetime.*
import java.time.format.DateTimeFormatter

fun createTimeStamp(timeZone: TimeZone = TimeZone.UTC) = run {
    val now = Clock.System.now()
   now.toLocalDateTime(timeZone)
}

fun LocalDateTime.durationSince(): String = run {
    val now = Clock.System.now()
    val period = toInstant(TimeZone.UTC)
        .periodUntil(now, TimeZone.UTC)

    val usingSecondUnit = period.seconds in 1..59 && period.minutes == 0
    val usingMinuteUnit = period.minutes in 1..59 && period.hours == 0
    val usingHourUnit = period.hours in 1..23 && period.days == 0
    val usingDayUnit = period.days in 1..6 && period.months == 0
    val usingWeekUnit = period.days in 7..30 && period.months == 0
    val usingMonthUnit = period.months in 1..11 && period.years == 0

    when {
        usingSecondUnit -> "${period.seconds} seconds ago"
        usingMinuteUnit -> "${period.minutes} minutes ago"
        usingHourUnit -> "${period.hours} hours ago"
        usingDayUnit -> "${period.days} days ago"
        usingWeekUnit -> "${period.days / 7} weeks ago"
        usingMonthUnit -> "${period.months} months ago"
        else -> "$dayOfMonth-$monthNumber-$year"
    }
}

fun LocalDateTime.breakDown() = run {
    "$dayOfMonth ${month.toEnglishLocal()} $year, ${hour.addZeroPrefix()}:${minute.addZeroPrefix()}"
}

private fun Int.addZeroPrefix() = if (this < 10) "0$this" else this.toString()

private fun Month.toEnglishLocal() = when(this) {
    java.time.Month.JANUARY -> "January"
    java.time.Month.FEBRUARY -> "February"
    java.time.Month.MARCH -> "March"
    java.time.Month.APRIL -> "April"
    java.time.Month.MAY -> "May"
    java.time.Month.JUNE -> "June"
    java.time.Month.JULY -> "July"
    java.time.Month.AUGUST -> "August"
    java.time.Month.SEPTEMBER -> "September"
    java.time.Month.OCTOBER -> "October"
    java.time.Month.NOVEMBER -> "November"
    else -> "December"
}

@Deprecated(
    message = "Since API moved for public used (no longer used for thesis), this function is deprecated.",
    replaceWith = ReplaceWith("toEnglishLocal()")
)
private fun Month.toIndonesianLocal() = when(this) {
    java.time.Month.JANUARY -> "Januari"
    java.time.Month.FEBRUARY -> "Februari"
    java.time.Month.MARCH -> "Maret"
    java.time.Month.APRIL -> "April"
    java.time.Month.MAY -> "Mei"
    java.time.Month.JUNE -> "Juni"
    java.time.Month.JULY -> "Juli"
    java.time.Month.AUGUST -> "Agustus"
    java.time.Month.SEPTEMBER -> "September"
    java.time.Month.OCTOBER -> "Oktober"
    java.time.Month.NOVEMBER -> "November"
    else -> "Desember"
}

fun String.fromGeolocationResponseToLocal(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ")
    val dateTime = java.time.LocalDateTime.parse(this, formatter).toKotlinLocalDateTime()
    return dateTime.time
}

fun ApplicationCall.getDateTimeBasedOnIp(): LocalTime {
    //just in case if reach the limit of the API due to free tier
    return try {
        val userIp = request.header("X-Forwarded-For") ?: request.local.remoteHost
        val geolocationApiKey = System.getenv("GEOLOCATION_API_KEY")
        val api = IPGeolocationAPI(geolocationApiKey)

        val geoParams = GeolocationParams.builder()
        geoParams.withIPAddress(userIp)
        geoParams.withFields("time_zone")

        val response = api.getGeolocation(geoParams.build())

        return if (response != null) {
            response.timezone.currentTime.fromGeolocationResponseToLocal()
        } else {
            val now = Clock.System.now()
            now.toLocalDateTime(TimeZone.of("Asia/Jakarta")).time
        }
    } catch (e: Exception) {
        val now = Clock.System.now()
        now.toLocalDateTime(TimeZone.of("Asia/Jakarta")).time
    }
}