package storage

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import io.ktor.http.*

class GoogleCloudStorageService(
    private val storage: StorageOptions
): CloudStorageService {
    override fun ByteArray.uploadFile(filePath: String): String {
        val service = storage.service
        val fileName = "$filePath${NanoIdUtils.randomNanoId()}"
        val bucketName = if (System.getenv("ENV") == "DEV") {
            "beu-dev"
        } else {
            "beu-prod"
        }
        val blob = BlobInfo.newBuilder(bucketName, fileName)
            .setContentType(ContentType.Image.JPEG.toString())
            .build()

        service.create(blob, this)
        val mediaLink = service.get(bucketName, fileName).mediaLink
        return mediaLink
    }
}