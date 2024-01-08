package com.teknophase.chat.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    fun build(baseDomain: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseDomain)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}