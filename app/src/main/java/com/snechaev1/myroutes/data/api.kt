package com.snechaev1.myroutes.data

class ApiException(val appError: AppError, val apiMessage: String? = null) : RuntimeException(appError.message)
data class AppError(val message: String = "", val code: Int? = null)

class ApiResponse<out T>(val message: String, val data: T?) {
    val dataNotNullOrEmpty get() = data != null && data.toString().isNotEmpty()
}

data class ApiResource<out T>(val status: Status, val data: T?, val message: String?, val exc: ApiException?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
    companion object {
        fun <T> success(message: String? = null, data: T? = null): ApiResource<T> {
            return ApiResource(Status.SUCCESS, data, message, null)
        }
        fun <T> error(message: String, exc: ApiException?): ApiResource<T> {
            return ApiResource(Status.ERROR, null, message, exc)
        }
        fun <T> loading(): ApiResource<T> {
            return ApiResource(Status.LOADING, null, null, null)
        }
    }
}

