import Version.commonsCodecVersion
import Version.exposedVersion
import Version.hikariVersion
import Version.jnanoidVersion
import Version.kotlinVersion
import Version.logbackVersion

object App {
    const val version = "1.0.0"
    const val group = "com.kylix"
}

object Version {
    const val kotlinVersion = "1.9.22"
    const val ktorVersion = "2.3.7"
    const val exposedVersion = "0.41.1"
    const val postgresqlVersion = "42.2.23"
    const val koinVersion = "3.5.3"
    const val hikariVersion = "5.0.1"
    const val jnanoidVersion = "2.0.0"
    const val logbackVersion = "1.4.11"
    const val commonsCodecVersion = "1.15"
}

object Libs {
    object Ktor {
        const val core = "io.ktor:ktor-server-core-jvm"
        const val auth = "io.ktor:ktor-server-auth-jvm"
        const val jwt = "io.ktor:ktor-server-auth-jwt-jvm"
        const val cn = "io.ktor:ktor-server-content-negotiation-jvm"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json-jvm"
        const val resources = "io.ktor:ktor-server-resources"
        const val cors = "io.ktor:ktor-server-cors-jvm"
        const val netty = "io.ktor:ktor-server-netty-jvm"

        const val logback = "ch.qos.logback:logback-classic:$logbackVersion"

        const val test = "io.ktor:ktor-server-tests-jvm"
    }

    object Exposed {
        const val core = ("org.jetbrains.exposed:exposed-core:$exposedVersion")
        const val jdbc = ("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    }

    object Database {
        const val postgresql = "org.postgresql:postgresql:42.2.23"
        const val hikari = "com.zaxxer:HikariCP:$hikariVersion"
    }

    object Koin {
        const val koin = "io.insert-koin:koin-core:3.1.2"
        const val koinKtor = "io.insert-koin:koin-ktor:3.1.2"
    }

    object Test {
        const val junit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
    }

    object Util {
        const val jnanoid = "com.aventrix.jnanoid:jnanoid:$jnanoidVersion"
        const val commonsCodec = "commons-codec:commons-codec:$commonsCodecVersion"
    }
}