package org.usp.barboza.visioaux.https

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val REQUEST_TIMEOUT: Long = 30

    private var retrofitInstance: Retrofit? = null

    const val BASE_URL: String = "https://0386-2001-818-dccc-5b00-c197-3adf-28-857b.ngrok-free.app/api/"

    private val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
    }.build()

    fun getRetrofit(): Retrofit {
        if (retrofitInstance == null) {
            val contentType = "application/json".toMediaType()

            retrofitInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(jsonSerde.asConverterFactory(contentType)) // should add it at last
                .build()
        }

        return retrofitInstance!!
    }

}