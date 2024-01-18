package storage

interface CloudStorageService {
    suspend fun uploadFile(fileName: String, fileBytes: ByteArray): String
}