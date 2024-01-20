package di

import com.google.auth.Credentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.StorageOptions
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import database.DatabaseFactory
import org.koin.dsl.module
import security.hashing.HashingService
import security.hashing.SHA256HashingService
import security.token.JWTTokenService
import security.token.TokenService
import storage.CloudStorageService
import storage.GoogleCloudStorageService
import java.io.FileInputStream
import java.net.URI

val databaseModule = module {
    single {
        DatabaseFactory(get())
    }

    single {
        val config = HikariConfig()
        config.apply {
            driverClassName = System.getenv("JDBC_DRIVER")
            maximumPoolSize = 6
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            jdbcUrl = if(System.getenv("ENV") == "DEV") {
                System.getenv("DATABASE_URL")
            } else {
                val uri = URI(System.getenv("DATABASE_URL"))
                val username = uri.userInfo.split(":").toTypedArray()[0]
                val password = uri.userInfo.split(":").toTypedArray()[1]
                "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=require&user=$username&password=$password"
            }

            validate()
        }
        HikariDataSource(config)
    }
}

val storageModule = module {
    single {
        val currentDir = System.getProperty("user.dir")
        val credentialsPath = "$currentDir/core/src/main/resources/beu.json" // Replace with the actual path to your key file
        val serviceAccountStream = FileInputStream(credentialsPath)
        val credentials = GoogleCredentials.fromStream(serviceAccountStream)

        StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
    }

    single<CloudStorageService> {
        GoogleCloudStorageService(get())
    }
}

val tokenModule = module {
    single<TokenService> { JWTTokenService(get()) }
    single<HashingService> { SHA256HashingService() }
}