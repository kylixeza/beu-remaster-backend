package util

import io.ipgeolocation.api.GeolocationParams
import io.ipgeolocation.api.IPGeolocationAPI
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun ApplicationCall.getPreferConsumeAt(): PreferConsumeAt {

    val userIp = request.header("X-Forwarded-For") ?: request.local.remoteHost
    val geolocationApiKey = System.getenv("GEOLOCATION_API_KEY")
    val api = IPGeolocationAPI(geolocationApiKey)

    val geoParams = GeolocationParams.builder()
    geoParams.withIPAddress(userIp)
    geoParams.withFields("time_zone")

    val response = api.getGeolocation(geoParams.build())

    val currentTime = if (response != null) {
        response.timezone.currentTime.fromGeolocationResponseToLocal()
    } else {
        val now = Clock.System.now()
        now.toLocalDateTime(TimeZone.of("Asia/Jakarta")).time
    }

    return when (currentTime.hour) {
        in 5..9 -> PreferConsumeAt.BREAKFAST
        in 10..14 -> PreferConsumeAt.LUNCH
        in 15..17 -> PreferConsumeAt.SNACK
        in 18..24 -> PreferConsumeAt.DINNER
        in 0..4 -> PreferConsumeAt.DINNER
        else -> PreferConsumeAt.SNACK
    }
}