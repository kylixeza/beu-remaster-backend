package storage

import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import io.ktor.http.*

class GoogleCloudStorageService(
    private val storage: StorageOptions
): CloudStorageService {
    override suspend fun uploadFile(fileName: String, fileBytes: ByteArray): String {
        val service = storage.service
        val bucketName = if (System.getenv("ENV") == "DEV") {
            "beu-dev"
        } else {
            "beu-prod"
        }
        val blob = BlobInfo.newBuilder(bucketName, fileName)
            .setContentType(ContentType.Image.JPEG.toString())
            .build()

        service.create(blob, fileBytes)
        return service.get(bucketName, fileName).mediaLink
    }
}