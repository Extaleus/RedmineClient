package com.example.redmineclient

import com.example.redmineclient.App.Companion.getAuthData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val BASE_URL = "https://rm.stagingmonster.com/"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpBuilder = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)

    @Provides
    fun provideRedmineApi(): RedmineApi {
        okHttpBuilder.addInterceptor(
            BasicAuthInterceptor()
        )

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build())
            .build()
            .create(RedmineApi::class.java)
    }
}

class BasicAuthInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", Credentials.basic(getAuthData().first, getAuthData().second))
            .build()
        return chain.proceed(authenticatedRequest)
    }
}