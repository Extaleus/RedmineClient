package com.example.redmineclient

import android.util.Log
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

const val BASE_URL = "https://rm.stagingmonster.com/"

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
            BasicAuthInterceptor(
                "maxim.danilichenko",
                "Jackp0t For A Sucker 1337"
            )
        )

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build())
            .build()
            .create(RedmineApi::class.java)
    }
}

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private val credentials: String = Credentials.basic(username, password)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", credentials).build()
        val response: Response = chain.proceed(authenticatedRequest)
        if (!response.isSuccessful) {
            Log.e("my", "CHECK YOUR INFO")
        }
        return response
    }
}