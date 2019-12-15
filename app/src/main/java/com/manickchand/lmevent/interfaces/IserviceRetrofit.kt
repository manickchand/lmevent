package com.manickchand.lmevent.interfaces

import com.manickchand.lmevent.model.CheckinDTO
import com.manickchand.lmevent.model.Event
import retrofit2.Call
import retrofit2.http.*

interface IserviceRetrofit {

    @GET("events")
    fun getAllEvents(): Call<List<Event>>

    @GET("events/{id}")
    fun getEventById(@Path("id") id:Int): Call<Event>

    @Headers( "content-type: application/json;" )
    @POST("checkin")
    fun eventCheckin(@Body body:CheckinDTO):Call<CheckinDTO>
}