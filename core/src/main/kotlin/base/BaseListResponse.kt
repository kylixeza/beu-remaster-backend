package base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseListResponse<T>(
	@SerialName("status_code")
	var statusCode: Int = 0,
	val message: String = "",
	val count: Int = 0,
	val data: T?
)
