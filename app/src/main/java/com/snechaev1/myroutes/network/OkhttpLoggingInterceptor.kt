package com.snechaev1.myroutes.network

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okhttp3.internal.platform.Platform
import okhttp3.logging.internal.isProbablyUtf8
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class OkhttpLoggingInterceptor @JvmOverloads constructor(
    private val logger: Logger = Logger.DEFAULT
) : Interceptor {

    fun interface Logger {
        fun log(message: String)

        companion object {
            /** A [Logger] defaults output appropriate for the current platform. */
            @JvmField
            val DEFAULT: Logger = DefaultLogger()
            private class DefaultLogger : Logger {
                override fun log(message: String) {
                    Platform.get().log(message)
                }
            }
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val requestBody = request.body
        val connection = chain.connection()
        var requestStartMessage =
            ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
        if (requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }
        logger.log(requestStartMessage)

        if (requestBody == null) {
            logger.log("--> END ${request.method}")
        } else if (bodyHasUnknownEncoding(request.headers)) {
            logger.log("--> END ${request.method} (encoded body omitted)")
        } else if (requestBody.isDuplex()) {
            logger.log("--> END ${request.method} (duplex request body omitted)")
        } else if (requestBody.isOneShot()) {
            logger.log("--> END ${request.method} (one-shot body omitted)")
        } else {
            val buffer = Buffer()
            requestBody.writeTo(buffer)

            val contentType = requestBody.contentType()
            val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

            logger.log("")
            if (buffer.isProbablyUtf8()) {
                logger.log(buffer.readString(charset))
                logger.log("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
            } else {
                logger.log(
                    "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()
        logger.log(
            "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms)")

        if (!response.promisesBody()) {
            logger.log("<-- END HTTP")
        } else if (bodyHasUnknownEncoding(response.headers)) {
            logger.log("<-- END HTTP (encoded body omitted)")
        } else {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer
            val contentType = responseBody.contentType()
            val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

            if (!buffer.isProbablyUtf8()) {
                logger.log("")
                logger.log("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                return response
            }

            if (contentLength != 0L) {
                logger.log("")
                logger.log(buffer.clone().readString(charset))
            }

            logger.log("<-- END HTTP (${buffer.size}-byte body)")

        }

        return response
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

}
