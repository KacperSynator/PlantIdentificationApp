package com.example.plantidentificationapp.api

import com.example.plantidentificationapp.api.model.ResponseIdentify
import com.example.plantidentificationapp.api.request.IdentifyRequest
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface PlantAPI {
    @POST("v2/identify")
    suspend fun identify(@Body request : IdentifyRequest) :  ResponseIdentify
}

fun createPlantAPIClient() : PlantAPI{
    return Retrofit.Builder()
        .baseUrl("https://api.plant.id/")
        .client(createClient())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(PlantAPI::class.java)
}

fun createClient() : OkHttpClient
{
    val loggingInterceptor = HttpLoggingInterceptor(

    )
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}