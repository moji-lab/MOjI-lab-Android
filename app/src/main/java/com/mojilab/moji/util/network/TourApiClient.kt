package com.mojilab.moji.util.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TourApiClient {

    // MOJI BaseURL
    var BASE_URL = "http://api.visitkorea.or.kr/openapi/service/"
    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
