package com.manickchand.lmevent.interfaces

import com.manickchand.lmevent.model.Event
import retrofit2.Call
import retrofit2.http.GET

interface IserviceRetrofit {

    @GET("events")
    fun getAllEvents(): Call<List<Event>>
}