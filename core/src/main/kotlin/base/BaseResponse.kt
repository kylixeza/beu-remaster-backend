package base

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
	@field:SerializedName("status_code")
	val statusCode: Int = 0,
	val message: String = "",
	val data: T,
)
