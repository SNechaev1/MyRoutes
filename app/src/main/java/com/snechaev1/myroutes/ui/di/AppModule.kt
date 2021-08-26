package com.snechaev1.myroutes.ui.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.snechaev1.myroutes.BuildConfig
import com.snechaev1.myroutes.network.ApiService
import com.snechaev1.myroutes.network.OkhttpLoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    var authToken = ""

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .also(this::initHeadersInterception)
//            .also(this::initErrorsInterception)
            .also(this::initLogs)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_PATH)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    private fun initHeadersInterception(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chain ->
//            val lang = BaseApp.getLang()
            val version = BuildConfig.VERSION_NAME.replace(".", "", ignoreCase = false)

            Timber.d("authToken: $authToken")

            val requestBuilder = chain.request().newBuilder()

            chain.proceed(requestBuilder
                .addHeader("os-version", "11")
                .addHeader("app-version", version)
                .addHeader("device-type", "android")
//                .addHeader("Authorization", "Bearer $authToken")
                .addHeader("Content-Type", "application/json")
                .build())
        }
    }

    private fun initErrorsInterception(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            if (!response.isSuccessful)
                Timber.d("ErrorsInterception: ${response.code}")
            response
        }
    }

    private fun initLogs(builder: OkHttpClient.Builder) {
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(OkhttpLoggingInterceptor())
        } else {
            builder.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE })
        }
    }

}