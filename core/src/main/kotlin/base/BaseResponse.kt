package base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
	@SerialName("status_code")
	val statusCode: Int = 0,
	val message: String = "",
	val data: T?
	
)
