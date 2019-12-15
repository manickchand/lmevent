package com.manickchand.lmevent.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInit {

    fun getClient():Retrofit{

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}