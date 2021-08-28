package com.snechaev1.myroutes.network

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.data.model.ApiException
import com.snechaev1.myroutes.data.model.AppError
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

fun ApiException.defaultHandler(context: Context) {
    val message: String = when (this.appError.code) {
                404 -> context.getString(R.string.no_content)
                else -> this.appError.message
            }
    Timber.d("defaultHandler: $message")
    Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
}

class ErrorInterpreter {
    fun interpret(context: Context, throwable: Throwable): ApiException {
        Timber.d("interpretExc: $throwable")
        return when (throwable) {
//            is ApiException -> throwable
            is SocketTimeoutException -> ApiException(AppError(context.resources.getString(R.string.connection_problems), 600))
            is SSLException -> ApiException(AppError(context.resources.getString(R.string.certificate_problem), 600))
            is UnknownHostException -> ApiException(AppError(context.resources.getString(R.string.connection_problems), 600))
            is HttpException -> formatHttpException(context, throwable)
            is NoSuchElementException -> ApiException(AppError(context.resources.getString(R.string.no_content), 204))
            is JsonSyntaxException -> ApiException(AppError(context.resources.getString(R.string.default_problems), 600))
            is ConnectException -> ApiException(AppError(context.resources.getString(R.string.connection_problems), 600))
            else -> ApiException(AppError(context.resources.getString(R.string.default_problems), 600))
        }
    }

    private fun formatHttpException(context: Context, throwable: HttpException): ApiException {
        val code = throwable.response()?.code()
        var apiMessage = throwable.response()?.message() ?: ""

        throwable.response()?.errorBody()?.string()?.let { errBody ->
            try {
                JsonParser().parse(errBody)
                        .asJsonObject["message"]
                        .asString
                        .let { apiMessage = it }
            } catch (exc: Exception) {
                Timber.d("parsing ErrorBody: ${exc.message}")
            }
        }

        Timber.d("formatHttpException code: $code , message: $apiMessage")

        val message = when (apiMessage) {
            "COULD_NOT_DEFINE_LOCATION" -> context.getString(R.string.could_not_define_location)
            "INVALID_PARAMS" -> context.getString(R.string.invalid_params)
            "INTERNAL_SERVER_ERROR" -> context.getString(R.string.default_problems)
            else -> context.getString(R.string.default_problems)
        }

        return ApiException(AppError(message, code))
    }

}