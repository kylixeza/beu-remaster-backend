package base

import com.google.gson.annotations.SerializedName

data class BaseListResponse<T>(
	@field:SerializedName("status_code")
	var statusCode: Int = 0,
	val message: String = "",
	val count: Int = 0,
	val data: T?
)
