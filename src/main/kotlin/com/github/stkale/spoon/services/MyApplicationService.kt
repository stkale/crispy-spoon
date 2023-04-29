package com.github.stkale.spoon.services

import com.intellij.openapi.components.Service
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Service
class MyApplicationService {
    @OptIn(ExperimentalSerializationApi::class)
    fun getCharonService(): CharonService {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", "application/json")
                val request = builder.build()
                chain.proceed(request)
            }.build()

        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://moodle.taltech.ee/mod/charon/api/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(httpClient)
            .build()

        return retrofit.create(CharonService::class.java)
    }
}
