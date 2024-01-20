package storage

interface CloudStorageService {
    fun ByteArray.uploadFile(filePath: String): String
}